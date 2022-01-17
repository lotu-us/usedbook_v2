const baseUrl = window.location.pathname;   //posts    //posts/novel
var id = baseUrl.replace("/post/detail/", "");

//update.js와 유사
$(document).ready(function(){
    loadPost();
    document.querySelector("#updateButton").href="/post/update/"+id;      //추가됨
});

//update.js와 같음
function loadPost(){
    $.ajax({
        url: "/api/post/"+id,
        type: "get",
        success: function(data){
            replacePost(data.post);
            replacePostFiles(data.postFileNames);
        },
        error: function(error){
            alert(error.responseText);
            window.location.replace("/posts");
        }
    });
}


function replacePost(post){
    document.querySelector("#title").innerHTML = post.title;
    document.querySelector("#category").innerHTML = post.categoryKor;
    document.querySelector("#price").innerHTML = post.price;
    document.querySelector("#stock").innerHTML = post.stock;
    document.querySelector("#content").innerHTML = post.content;
    document.querySelector("#likeCount").innerHTML = post.likeCount;
    document.querySelector("#viewCount").innerHTML = post.viewCount;
    document.querySelector("#createTime").innerHTML = post.createTime;
}

//update.js와 같음
function replacePostFiles(postFileNames){
    files.init(postFileNames);
}



function deletePost(){

    var question = confirm("게시글을 삭제하시겠습니까?");
    if(question == false){
        return;
    }

    $.ajax({
        url: "/api/post/"+id,
        type: "delete",
        success: function(data){
            window.location.replace("/posts");
        },
        error: function(error){
            alert(error.responseText);
            window.location.replace("/posts");
        }
    });

}