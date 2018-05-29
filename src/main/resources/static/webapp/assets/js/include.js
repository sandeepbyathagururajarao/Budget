
function validateLogin() {
    var UID = sessionStorage.getItem("UID");
    var UTPE = sessionStorage.getItem("UTPE");
    if(UID == null || UTPE == null) {
        location.href = "login.html";
        return;
    }
}
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

function populateRecordCount() {
    var dashPI =document.getElementById("dash_pi");
    var dashMaster = document.getElementById("dash_master");
    var dashApproval = document.getElementById("dash_approval");
    var dashUser = document.getElementById("dash_user");
    var menuPI = document.getElementById("menu_pi");
    var menuApproval = document.getElementById("menu_approval");
    $.ajax({
        method: "GET",
        url: path,
        cache: false,
        success: function(data){

        },
        error:function(xhr,status,err){
            alert("Error:"+err);
        }
    });
}

function getFormData($form, toStringify){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};
    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });
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

function submitPurchaseData(path) {
    var forms = $('form');
    var firstForm = getFormData($(forms[0]), false);
    var lastForm = getFormData($(forms[forms.length-1]), false);
    var mergedJSON = mergeJSON(firstForm, lastForm);

    var subItems = new Array();
    var indexed_array = mergedJSON;
    for(var formCnt = 1; formCnt<forms.length-1; formCnt++) {
        var subItemForm = getFormData($(forms[formCnt]), false);
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

