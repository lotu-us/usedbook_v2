function editFormSubmit(){
    //이미지 체크 (slider.js)
    var imgCheck = files.sizeMinCheck();
    if(imgCheck == false){
        return;
    }

    var form = $("#editForm")[0];
    var formData = new FormData(form);

    //(slider.js)
    files.map.forEach(function(v, k){
        formData.append("fileList", v);
    });

    $.ajax({
        url: "/post",
        type: "post",
        data: formData,
        contentType: false,
        processData: false,
        success: function(lists){
            console.log(lists.length);
            initInputs();
            if(lists.length > 0){
                lists.forEach(function(list){
                    if(list.field == "fileList"){
                        alert("이미지는 최소 1개 이상 업로드 해야합니다.");
                    }
                    $("#"+list.field).addClass("is-invalid");
                    $("#"+list.field+"Help").text(list.message);
                    $("#"+list.field+"Help").addClass("ani");
                });
            }
            if(lists.length == 0){
                window.location.replace("/posts");
            }
        },
        error: function(error){
            alert(error);
        }
    });

}


function initInputs(){
    var inputs = ["title", "category", "price", "stock", "content"];
    inputs.forEach(function(input){
        $("#"+input+"Help").css("display", "none");
    });
}


$("#title, #category, #price, #stock, #content").on("click", function(){
    if($(this).hasClass("is-invalid")){
        $(this).removeClass("is-invalid");

        var thisid = $(this).attr("id");
        $("#"+thisid+"Help").removeClass("ani");
    }
});