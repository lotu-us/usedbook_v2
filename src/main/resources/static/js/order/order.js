
$(document).ready(function(){
    loadBasketToOrder();
});


function loadBasketToOrder(){
    $.ajax({
        url: "/api/basketToOrder",
        type: "get",
        success: function(data){
            $("table tbody *").replaceWith();
            $("table tbody").append(addTR(data));
        },
        error: function(error){
            alert(error.responseText);
            window.location.replace("/order/basket");
        }
    });
}







function addTR(orderBasketList){
    var result = "";
    var sum = 0;

    orderBasketList.forEach(function(basket){

        sum = sum + (basket.count * basket.price);

        result = result + `
            <tr>
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
                </td>

                <td><span class="buyPrice won">${basket.count * basket.price}</span></td>
                <input type="hidden" class="postStock" value="${basket.stock}">

                <input type="hidden" class="postId" value="${basket.id}">
            </tr>
            `;
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

    document.querySelectorAll(".buyPrice").forEach(function(price){
        sum = sum + parseInt(price.innerHTML);
    });

    if(initvalue != null){
        sum = initvalue;
    }

    $(".totalPrice").text(sum);
}
