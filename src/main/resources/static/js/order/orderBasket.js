
$(document).ready(function(){
    loadBasket();
});


function loadBasket(){
    $.ajax({
        url: "/api/order/basket",
        type: "get",
        success: function(data){
            $("table tbody *").replaceWith();
            $("table tbody").append(addTR(data));
        },
        error: function(error){

        }
    });
}


function updateBasket(postId, count, event){

    $.ajax({
        url: "/api/order/basket/"+postId+"/"+count,
        type: "put",
        success: function(data){

        },
        error: function(error){
            alert(error.responseText);
        }
    });
}


function deleteBasket(postId, event){

    $.ajax({
        url: "/api/order/basket/"+postId,
        type: "delete",
        success: function(data){
             if(data > 0){
                $(event.target).parents("tr").remove();
                changeTotalPrice();
            }
        },
        error: function(error){
            alert(error.responseText);
        }
    });
}


function order(){
    var boxesChecked = document.querySelectorAll("input[type='checkbox']:checked");
    if(boxesChecked.length -1 == 0){
        alert("상품을 선택해주세요");
        return;
    }

    var objarr = new Array();
    for(var i=1; i<boxesChecked.length; i++){
        var obj = {
            postid:0,
            count:0
        }
        obj.postid = boxesChecked[i].value;
        obj.count = $(boxesChecked[i]).parents("tr").find(".orderCount div").text();
        objarr.push(obj);
    }

    $.ajax({
        url: "/api/order",
        type: "post",
        contentType: 'application/json',
        data: JSON.stringify(objarr),
        success: function(data){

        },
        error: function(error){
            alert(error.responseText);
        }
    });
}







function addTR(orderBasketList){
    var result = "";
    var sum = 0;

    orderBasketList.forEach(function(basket){
        result = result + `
            <tr>
                <th scope="row"><input class="form-check-input" type="checkbox" value="${basket.id}" checked></th>

                <td>
                    <div style="float:left;">
                        <div><a href="/post/detail/${basket.id}">${basket.title}</a></div>
                        <div>판매자 : ${basket.writer}</div>
                        <div>판매가격 : <span class="postPrice won">${basket.price}</span></div>
                        <div><span style="float:left">구매수량 : </span>
                            <div class="orderCount input-group">
                                <button class="btn btn-sm btn-outline-secondary" onclick="changeCount('minus', ${basket.id}, event)">-</button>
                                <div class="form-control">${basket.count}</div>
                                <button class="btn btn-sm btn-outline-secondary" onclick="changeCount('plus', ${basket.id}, event)">+</button>
                            </div>
                        </div>
                    </div>

                    <div style="float:right; font-size:1em;">
                        <button class="btn btn-sm btn-outline-primary" onclick="deleteBasket(${basket.id}, event)">삭제</button>
                    </div>
                </td>

                <td><span class="buyPrice won">${basket.count * basket.price}</span></td>
                <input type="hidden" class="postStock" value="${basket.stock}">
            </tr>
            `;

        sum = sum + (basket.count * basket.price);
    });
    changeTotalPrice(sum);

    return result;
}











//addOrderBasket.js와 유사
function changeCount(text, postId, event){
    var trs = $(event.target).parents("tr");
    var countDiv = trs.find(".orderCount.input-group div");
    var count = parseInt(countDiv.html());
    var buttons = trs.find(".orderCount button");

    var postStock = parseInt(trs.find(".postStock").val());
    var postPrice = parseInt(trs.find(".postPrice").text());
    var buyPrice = trs.find(".buyPrice");

    if(text == "minus" && count > 1){
        countDiv.html(count -1);
    }
    if(text == "plus" && count < postStock){
        countDiv.html(count +1);
    }

    count = parseInt(countDiv.html());
    buyPrice.text(count * postPrice);

    changeTotalPrice();
    updateBasket(postId, count);

    buttons[0].disabled = false;
    buttons[1].disabled = false;
    if(count <= 1 ){
        buttons[0].disabled = true;
    }

    if(count >= postStock){
        buttons[1].disabled = true;
    }
}


function changeTotalPrice(initvalue){
    var sum = 0;
    var trs = $("input[type='checkbox']:checked").parents("tr");

    trs.find(".buyPrice").each(function(){
        sum = sum + parseInt(this.innerHTML);
    });

    if(initvalue != null){
        sum = initvalue;
    }

    $(".totalPrice").text(sum);
}


$(document).on("click", "input[type='checkbox']", function(){
    var boxes = document.querySelectorAll("input[type='checkbox']");
    var boxesChecked = document.querySelectorAll("input[type='checkbox']:checked");

    if(boxesChecked.length -1 == 0){
        document.querySelector("#checkAll").checked = false;
    }
    if(boxes.length -1 == boxesChecked.length){
        document.querySelector("#checkAll").checked = true;
    }

    changeTotalPrice();
});


function checkboxAll(selectAll){
    var boxes = document.querySelectorAll("input[type='checkbox']");

    boxes.forEach(function(box){
        box.checked = selectAll.checked;
    });
}