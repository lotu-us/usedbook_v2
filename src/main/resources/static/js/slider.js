 var cardSwipers = new Map();
 $('.card-slider .swiper').each(function(i) {
   var thisID = $(this).attr('id');

   cardSwipers.set('cardSlider_'+thisID, new Swiper('#'+thisID, {
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
    }
   ));
 });
