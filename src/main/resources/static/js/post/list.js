const baseUrl = window.location.pathname;   //posts    //posts/novel
const queryString = window.location.search;       //?page=4

$(document).ready(function(){
    loadList(event);
});

$("#title, #writer, #createtime, #viewcount").on("click", function(){
    loadList(event);
});

function loadList(event){

//    var id = $(event.target).attr("id");
//    var orderType = $(event.target).attr("class");

    $.ajax({
        url: "/api/"+baseUrl+queryString,
        type: "get",
        success: function(data){
            $(".head h3").text(data.pagination.categoryKor);
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
                    <a>${post.title}</a><span> (<span>${post.commentCount}</span>)</span>
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

