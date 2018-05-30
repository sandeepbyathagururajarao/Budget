sessionStorage.removeItem("UID");
sessionStorage.removeItem("UTPE");

function login() {
    var userId = document.getElementById("userId").value;
    var password = document.getElementById("password").value;
    $.ajax({
        method: "GET",
        cache: false,
        url: authenticate+userId,
        success: function(data){
            if(data != null && data.length > 0) {
                var id = data[0].id;
                var userType = data[0].userType;
                var respUserName = data[0].userId;
                var respPassword = data[0].password;
                if(userId === respUserName && password === respPassword) {
                    sessionStorage.setItem("UID",id);
                    sessionStorage.setItem("UTPE",userType);
                    location.href = "index.html";
                } else {
                    alert("Authetication failed, UserId or password is invalid");
                }
            }
        },
        error:function(xhr,status,err){
            alert("Authetication failed:"+err);
        }
    });
}
