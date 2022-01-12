function findSubmit(){
    var email = $.trim($("#email").val());
    var nickname = $.trim($("#nickname").val());

    $.ajax({
        type:"post",
        url:"/findPasswordCheck",
        contentType: 'application/x-www-form-urlencoded',
        data: {
            "email":email,
            "nickname":nickname
        },
        success: function(list){
            //[{"field":"nickname","message":"정보를 확인해주세요."}]
            if(list != "" && list[0].field == "email"){
                inputClass("email", "red");
                feedbackClass("find", "block", list[0].message);
            }
            if(list != "" && list[0].field == "nickname"){
                inputClass("email", "green");
                inputClass("nickname", "red");
                feedbackClass("find", "block", list[0].message);
            }
            if(list == ""){
                inputClass("email", "green");
                inputClass("nickname", "green");
                feedbackClass("find", "none", null);
                $("#findForm").submit();
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
$("#email, #nickname").on("keyup change", function(){
    var str = this.value.replace(" ", "");
    $(this).val(str);
});