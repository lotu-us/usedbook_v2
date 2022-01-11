var cardSliders = new Map();
$('.card-slider').each(function(i) {
    var thisID = $(this).attr("id");
    cardSliders.set('cardSlider_'+thisID, createCardSlider(thisID));
});

function createCardSlider(thisID){
    var selector = '#'+thisID+" .swiper";
    var cardSlider = new Swiper(selector, {
        loop: true,
        navigation: {
            nextEl: '#next_'+thisID,
            prevEl: '#prev_'+thisID,
        },
        lazy : {
            loadPrevNext : true, // 이전, 다음 이미지는 미리 로딩
        },
        breakpoints: {
             320: {  slidesPerView: 2,  spaceBetween: 10,  },
             500: {  slidesPerView: 3,  spaceBetween: 10,  },
             768: {  slidesPerView: 4,  spaceBetween: 10,  },
             1024: { slidesPerView: 5,  spaceBetween: 10,  },
             //반복되려면 최소 5개 이상의 슬라이드가 있어야한다!!
             //3개 슬라이드로 5개 테스트하면 next안눌려짐
        },
    });
    return cardSlider;
}

//===================================================================================
//===================================================================================
//===================================================================================

var thumbTopSliders = new Map();
var thumbBottomSliders = new Map();

$('.thumb-slider').each(function(i) {
    var thisID = $(this).attr("id");
    thumbBottomSliders.set('thumbBottomSlider_'+thisID, createThumbBottomSlider(thisID));
    thumbTopSliders.set('thumbTopSlider_'+thisID, createThumbTopSlider(thisID));
    //bottom이 위로 올라가야함. 안그러면 topslider생성 시 클릭 동작 안함
});

function createThumbTopSlider(thisID){
    var selector = '#'+thisID+" .thumb-top";
    var thumbTop = new Swiper(selector, {
        spaceBetween: 10,
        navigation: {
            nextEl: '#next_'+thisID,
            prevEl: '#prev_'+thisID,
        },
        thumbs: {
          swiper: thumbBottomSliders.get('thumbBottomSlider_'+thisID),
        },
    });
    return thumbTop;
}

function createThumbBottomSlider(thisID){
    var selector = '#'+thisID+" .thumb-bottom";
    var thumbBottom = new Swiper(selector, {
        spaceBetween: 10,
        slidesPerView: 5,
        freeMode: true,
        watchSlidesProgress: true,
    });
    return thumbBottom;
}

