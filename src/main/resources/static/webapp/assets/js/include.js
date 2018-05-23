
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
        success: function(data){

        },
        error:function(xhr,status,err){
            alert("Error:"+err);
        }
    });
}

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};
    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });
    indexed_array["createdDate"]=new Date();
    indexed_array["userId"]=sessionStorage.getItem("UID");
    return JSON.stringify(indexed_array);
}

function submitData(path) {
    var formJSON = getFormData($('form'));
    $.ajax({
        method: "PUT",
        contentType:"application/json; charset=utf-8",
        data : formJSON,
        url: path,
        success: function(data){
            alert(data);
        },
        error:function(xhr,status,err){
            alert("Error:"+err);
        }
    });
}

