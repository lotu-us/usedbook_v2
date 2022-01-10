function loginSubmit(){
    var email = $.trim($("#email").val());
    var password = $.trim($("#password").val());

    $.ajax({
        type:"post",
        url:"/loginCheck",
        contentType: 'application/x-www-form-urlencoded',
        data: {
            "email":email,
            "password":password
        },
        success: function(data){
            if(data != "" && data[0].field == "email"){
                inputClass("email", "red");
                inputClass("password", "red");
                feedbackClass("login", "block", data[0].message);
            }
            if(data != "" && data[0].field == "password"){
                inputClass("email", "green");
                inputClass("password", "red");
                feedbackClass("login", "block", data[0].message);
            }
            if(data == ""){  //오류가 없는 경우
               inputClass("email", "green");
               inputClass("password", "green");
               feedbackClass("login", "none", null);
               $("#loginForm").submit();
            }
        },
        error:function(error){
        }
    });
}


function inputClass(selector, color){
    if(color == "green"){    //초록색
        $("#"+selector).addClass("is-valid").removeClass("is-invalid");
    }
    if(color == "red"){  //빨간색
        $("#"+selector).addClass("is-invalid").removeClass("is-valid");
    }
}

function feedbackClass(selector, display, text){
    if(display == "block"){
        $("#"+selector+"Help").css("display", "block").text(text)
        .addClass("invalid-feedback").removeClass("valid-feedback");
    }
    if(display == "none"){
        $("#"+selector+"Help").css("display", "none");
    }
}



//공백제거
$("#email, #password").on("keyup change", function(){
    var str = this.value.replace(" ", "");
    $(this).val(str);
});