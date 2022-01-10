 $("#email, #nickname, #password").on("input", function(){
    registerCheck(this);
});

var validENP = false;   //Email Nickname Password
var validPPC = false;   //Password PasswordConfirm
function registerSubmit(){
    if(validENP && validPPC){   //submit가능
        $("#registerForm").submit();
    }
}


function registerCheck(element){
    var current = $(element).attr("id");
    var email = $.trim($("#email").val());
    var nickname = $.trim($("#nickname").val());
    var password = $.trim($("#password").val());

    $.ajax({
        type:"post",
        url:"/registerCheck",
        contentType: 'application/json',
        data: JSON.stringify({
            "email":email,
            "nickname":nickname,
            "password":password
        }),
        success: function(lists){
            validProcess(lists, current);
        },
        error:function(error){
        }
    });
}

function validProcess(lists, current){
    var currentValid;
    if(lists.length > 0){
        validENP = false;
         currentValid = lists.find(function(element){
            if(element.field === current){
                return true;
            }
        });     //currentValid = {field: 'email', message: '중복되는 이메일이 있습니다'}

        if(currentValid != undefined){
            inputClass(current, "red");
            feedbackClass(current, "block", currentValid.message);
        }else{
            inputClass(current, "green");
            feedbackClass(current, "none", null);
        }
    }else{
        validENP = true;
        inputClass(current, "green");
        feedbackClass(current, "none", null);
    }
}

//비밀번호, 비밀번호 확인 일치 체크
$("#password, #passwordConfirm").on("change keyup", function(){
    var password = $.trim($("#password").val());
    var passwordConfirm = $.trim($("#passwordConfirm").val());

    if(password != null){
        if(password != passwordConfirm){
            inputClass("passwordConfirm", "red");
            feedbackClass("passwordConfirm", "block", "비밀번호가 일치하지 않습니다");
            validPPC = false;
        }else{
            inputClass("passwordConfirm", "green");
            feedbackClass("passwordConfirm", "none", null);
            validPPC = true;
        }
    }
});


//공백제거
$("#email, #nickname, #password, #passwordConfirm").on("keyup", function(){
    var str = this.value.replace(" ", "");
    $(this).val(str);
});

//button활성화
$("#email, #nickname, #password, #passwordConfirm").on("keyup", function(){
    if(validENP && validPPC){   //submit가능
        $("#registerForm button").attr('disabled', false);
    }else{
        $("#registerForm button").attr('disabled', true);
    }
});

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

