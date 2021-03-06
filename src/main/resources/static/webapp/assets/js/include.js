
function validateLogin() {
    var UID = sessionStorage.getItem("UID");
    var UTPE = sessionStorage.getItem("UTPE");
    if(UID == null || UTPE == null) {
        location.href = "login.html";
        return;
    }
}
var roleName = "";
if(sessionStorage.getItem("UTPE") == "1" ) {
	roleName = "Super Admin";
} else if(sessionStorage.getItem("UTPE") == "2" ) {
	roleName = "Admin";
    $("a[href$='user_super_admin.html']").remove();
} else {
	roleName = "User";
    $("a[href$='user_super_admin.html']").remove();
    $("a[href$='user_admin.html']").remove();
}
$(".brand-link").append("<br><span>" + roleName + "</span>");
validateLogin();
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


function removeRecord(id, path) {
	var r = confirm("Press ok button to delete!");
	if (r == true) {
		$.ajax({
		  method: "DELETE",
          cache: false,
		  url: path+id,
		  success: function(data){
			location.reload();
		  },
		  error:function(xhr,status,err){
			alert("Error:"+err);
		  }
		});
	}
}

function updateFieldsWithData(id, path) {
    $.ajax({
        method: "GET",
        cache: false,
        url: path+id,
        success: function(data){
            if(data != null) {
                for (var key in data) {
                    if (data.hasOwnProperty(key)) {
                        var val = data[key];
                        if(val != null) {
                            if(document.getElementById(key) != null) {
                                document.getElementById(key).value = val;
                            }
                        }
                    }
                }
            }
        },
        error:function(xhr,status,err){
            alert("Error:"+err);
        }
    });
}

var purchaseEditData = null;

function updatePurchaseItemFieldsWithData() {
    if(purchaseEditData != null) {
        var forms = $('form');
        updatePurchaseParentFormFields(forms[0]);
        updatePurchaseParentFormFields(forms[forms.length - 2]);
        updateChildFormFields(forms);
    }
}

function updatePurchaseParentFormFields($form) {
    var formElements = $form.elements;
    for(var elem in formElements) {
        loopParentItemForm(formElements[elem]);
    }
}

function loopParentItemForm(elem) {
    var val = purchaseEditData[elem.id];
    if(val != null) {
        $("#"+elem.id).val(val).trigger("change");
    }
}

function updateChildPurchaseFormFields($form, subItem) {
    var formElements = $form.elements;
    for(var elem in formElements) {
        loopChildItemForm(formElements[elem], subItem);
    }
}

function loopChildItemForm(elem, subItem) {
    var val = subItem[elem.id];
    if(val != null) {
        $(elem).val(val).trigger("change");
    }
}

function updateChildFormFields($forms) {
    var subItems = purchaseEditData["subItems"];
    if(subItems != null && subItems.length > 0) {
        for(var formCnt = 1; formCnt<$forms.length-2; formCnt++) {
            updateChildPurchaseFormFields($forms[formCnt], subItems[formCnt - 1]);
        }
    }
}

function getAndUpdatePurchaseItemData(id, path) {
    $.ajax({
        method: "GET",
        cache: false,
        url: path+id,
        success: function(data){
            if(data != null) {
                purchaseEditData = data;
                var subItems = purchaseEditData["subItems"];
                if(subItems != null) {
                    for(ctr=1;ctr<subItems.length;ctr++) {
                        $('.hide_on_click').click();
                    }
                }
            }
        },
        error:function(xhr,status,err){
            purchaseEditData = null;
            alert("Error:"+err);
        }
    });
}

function populateRecordCount() {
    //var userCount =document.getElementById("userCount");
    /*var dashMaster = document.getElementById("dash_master");
    var dashApproval = document.getElementById("dash_approval");*/
    //var dashUser = document.getElementById("dash_user");
    //var menuPI = document.getElementById("menu_pi");
    //var menuApproval = document.getElementById("menu_approval");
    var stateId = sessionStorage.getItem("ST");
    // For SuperAdmin
    if(stateId == null || stateId == "null") {
        stateId = "000";
    }
    $.ajax({
        method: "GET",
        url: dashboard +sessionStorage.getItem("UTPE")+"/"+sessionStorage.getItem("UID")+"/"+stateId,
        cache: false,
        success: function(data){
            var countValues = data.split("||");
            document.getElementById("dash_pi").innerText = countValues[1];
            document.getElementById("dash_user").innerText = countValues[0];
            document.getElementById("dash_approval").innerText = countValues[2];
        },
        error:function(xhr,status,err){
            alert("Error:"+err);
        }
    });
}

function getFormData($form, toStringify) {
    var validationArr = validateFormData($form);
    if(!validationArr) {
        return validationArr;
    }
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};
    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });
    if(indexed_array["id"] == "") {
        indexed_array["id"] = null;
    }
    if(indexed_array["id"] != null) {
        indexed_array["modifiedDate"]=new Date();
    } else {
        indexed_array["createdDate"]=new Date();
    }
    if(indexed_array["userId"] == null) {
        indexed_array["userId"] = sessionStorage.getItem("UID");
    }
    var output = indexed_array;
    if(toStringify == null) {
        output = JSON.stringify(indexed_array);
    }
    return output;
}

function validateFormData($form) {
    var result = true;
    $form.find("select").each(
        function(index){
            var input = $(this);
            var name = input.attr('name');
            var value = input.val();
            if(name != "unit") {
                if(value == "Select" || value == "" || value == null) {
                    alert("Please select a value for " + $("#" + name).prev("label").text());
                    result = false;
                }
            } else {
                input.val("");
            }
        }
    );
    return result;
}

function extend(src, dest) {
    Object.keys(src).forEach(function(key) {
        dest[key] = src[key];
    });
    return dest;
}

function mergeJSON(fromJSON, toJSON){
    return extend(fromJSON, toJSON);
}


function submitData(path) {
    var formJSON = getFormData($('form'));
    $.ajax({
        method: "PUT",
        contentType:"application/json; charset=utf-8",
        data : formJSON,
        cache: false,
        url: path,
        success: function(data){
            if(data != null) {
                alert("Data updated succesfully");
                history.back();
            }
        },
        error:function(xhr,status,err){
            alert("Error:"+err);
        }
    });
}

function updateApproveJSON(id, approvalStatus) {
    var approvalData = {};
    approvalData["id"] = id.substring(id.indexOf("_")+1, id.length);
    approvalData["approvalStatus"] = approvalStatus;
    return approvalData;
}

function approveData(path, indexed_array) {
    var formJSON = JSON.stringify(indexed_array);
    $.ajax({
        method: "POST",
        contentType:"application/json; charset=utf-8",
        data : formJSON,
        cache: false,
        url: path+"approve",
        success: function(data){
            if(data != null) {
                alert("Data updated succesfully");
                location.reload(true);
            }
        },
        error:function(xhr,status,err){
            alert("Error:"+err);
        }
    });
}

function submitPurchaseData(path) {
    var forms = $('form');
    var firstForm = getFormData($(forms[0]), false);
    if(firstForm == false) {
        return;
    }
    var lastForm = getFormData($(forms[forms.length-2]), false);
    if(lastForm == false) {
        return;
    }
    var mergedJSON = mergeJSON(firstForm, lastForm);

    var subItems = new Array();
    var indexed_array = mergedJSON;
    for(var formCnt = 1; formCnt<forms.length-2; formCnt++) {
        var subItemForm = getFormData($(forms[formCnt]), false);
        if(subItemForm == false) {
            return;
        }
        subItems.push(subItemForm);
    }

    indexed_array["subItems"] = subItems;
    var formData = JSON.stringify(indexed_array);

    $.ajax({
        method: "PUT",
        contentType:"application/json; charset=utf-8",
        data : formData,
        cache: false,
        url: path,
        success: function(data){
            if(data != null) {
                alert("Data updated succesfully");
                history.back();
            }
        },
        error:function(xhr,status,err){
            alert("Error:"+err);
        }
    });
}

function getSearchRecords(path, criteriaName, criteriaValue) {
    if(criteriaValue == null || criteriaValue == "") {
        criteriaValue = "@~all~@";
    }
    $.ajax({
        method: "GET",
        cache: false,
        url: path+"search/"+ criteriaValue + "/" + +sessionStorage.getItem("UTPE")+"/"+sessionStorage.getItem("UID"),
        success: function(data){
            $('#display_grid tbody').empty();
            if(data != null && data.length > 0) {
                for(ctr = 0; ctr < data.length; ctr++) {
                    var id = data[ctr].id;
                    var name = data[ctr][criteriaName];
                    $('#display_grid tbody').append('<tr><td>'+name+'</td><td class="text-right"><a href="master_item_view.html?id='+id+'"><i class="fas fa-eye icon-table"></i></a><a href="master_item_edit.html?id='+id+'"><i class="fas fa-pencil-alt icon-table" style="background: green"></i></a><i onclick=removeRecord('+id+',\''+items+'\') class="remove fas fa-trash-alt icon-table" style="background: red"></i></td></tr>');
                }
            }
        },
        error:function(xhr,status,err){
            alert("Error:"+err);
        }
    });
}

var randomString = function(length) {
    if(length == null) {
        length = 5;
    }
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    for(var i = 0; i < length; i++) {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return text;
};

function uploadFileEvent() {
    $("#btnSubmit").click(function (event) {
        event.preventDefault();
        var form = $('#fileUploadForm')[0];
        var data = new FormData(form);
        data.append('imageFile',form.imageFile.files[0]);
        $("#btnSubmit").prop("disabled", true);
        $.ajax({
            type: 'POST',
            url: upload,
            data: data,
            async: false,
            processData: false,
            contentType:false,
            cache: false,
            timeout: 600000,
            success: function (responseText) {
                var newFileName = responseText.substring(0,responseText.indexOf("_"));
                $('#image').val(newFileName);
                responseText = responseText.substring(responseText.indexOf("_")+1);
                alert(responseText);
                $("#btnSubmit").prop("disabled", false);
            },
            error: function (e) {
                alert("ERROR while uploading: "+e);
                $("#btnSubmit").prop("disabled", false);
            }
        });
    });
}
