const baseUrl = window.location.pathname;   //posts    //posts/novel
const queryString = window.location.search;       //?page=4

$(document).ready(function(){
    loadList(null, null);
});

$("#title, #writer, #createtime, #viewcount").on("click", function(){
    changeClass(this.id, this.className);
    loadList(this.id, this.className);
});

function loadList(e_id, e_class){

    var orderText = "";
    if(queryString == ""){  //쿼리스트링이 비어있으면 ?로 시작해야함
        orderText = "?";
    }else{
        orderText = "&";
    }

    if(e_id != null){
        orderText = orderText + "otext=" + e_id + "&otype=" + e_class;
    }

    $.ajax({
        url: "/api"+baseUrl+queryString+orderText,
        type: "get",
        success: function(data){
            $(".head h3").text(data.pagination.categoryKor);

            $("table tbody *").replaceWith();
            $(".pagination *").replaceWith();

            $("table tbody").append(addTR(data.posts));
            $(".pagination").append(addPagination(data.pagination));
        },
        error: function(error){

        }
    });

}



function addTR(posts){
    var result = "";
    posts.forEach(function(post){
        result = result + `
            <tr>
                <th scope="row">${post.id}</th>
                <td>${post.categoryKor}</td>
                <td style="text-overflow: ellipsis;">
                    <a href="/post/detail/${post.id}">${post.title}</a><span> (<span>${post.commentCount}</span>)</span>
                </td>
                <td>${post.writer}</td>
                <td>${post.createTime}</td>
                <td>${post.viewCount}</td>
            </tr>
            `;
    });
    return result;
}

function addPagination(pagination){
    var paramPage = new URLSearchParams(location.search).get("page");
    if(paramPage == null){
        paramPage = 1;
    }

    var result = `
    <li class="page-item  ${pagination.prevPage == false ? 'disabled' : ''}">
        <a class="page-link" href="${baseUrl}?page=${pagination.startPage-1}">Previous</a>
    </li>`;

    for(var num = pagination.startPage; num <= pagination.endPage; num++){
        result = result + `
        <li class="page-item  ${num == paramPage ? 'active' : ''}">
            <a class="page-link" href="${baseUrl}?page=${num}">${num}</a>
        </li>
        `;
    }

    result = result + `
    <li class="page-item  ${pagination.nextPage == false ? 'disabled' : ''}">
        <a class="page-link" href="${baseUrl}?page=${pagination.endPage+1}">Next</a>
    </li>
    `;

    return result;
}


function changeClass(e_id, e_class){
    if(e_class == "asc"){
        $("#"+e_id).removeClass("asc").addClass("desc");
        $("#"+e_id+" span").text("↓");
    }
    if(e_class == "desc"){
        $("#"+e_id).removeClass("desc").addClass("asc");
        $("#"+e_id+" span").text("↑");
    }
}