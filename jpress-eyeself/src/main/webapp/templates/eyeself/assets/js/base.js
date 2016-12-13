$(function(){
    //当滚动条的位置处于距顶部100像素以下时，跳转链接出现，否则消失
    $(function () {
        $(window).scroll(function(){
            if ($(window).scrollTop()>100){
                $(".fiex_up").fadeIn();
            }
            else
            {
                $(".fiex_up").fadeOut();
            }
        });

        //当点击跳转链接后，回到页面顶部位置

        $(".fiex_up").click(function(){
            $('body,html').animate({scrollTop:0},1000);
            return false;
        });
    });


	
	$(".nav_class").mousemove(function(){
	 	$(".nav_class_box").css("display","block");
	});

    $(".nav_class").mouseout(function(){
       $(".nav_class_box").css("display","none");
    });
	
	
	$(".nav_class_box_title").mouseover( function() {
		 $(".nav_class_morebox").css("display","none");
         $(this).children(".nav_class_morebox").css("display","block");
     }).mouseout( function(){
        $(".nav_class_morebox").css("display","none");
     });
	 

/*点击切换*/	
	var login_box_main_msgl=true;
	$(".login_box_main_msgl").click(function(){
	if(login_box_main_msgl == true){
			$(".login_box_main_msgl img:first-child").css("display","none");
			$(".login_box_main_msgl img:last-child").css("display","inline-block");
			login_box_main_msgl=false;
		}else{
			$(".login_box_main_msgl img:first-child").css("display","inline-block");
			$(".login_box_main_msgl img:last-child").css("display","none");
			login_box_main_msgl=true;
		}
	});
	
	
	var zc_box_main_msg=true;
	$(".zc_box_main_msg").click(function(){
	if(zc_box_main_msg == true){
			$(".zc_box_main_msg01").css("display","inline-block");
			$(".zc_box_main_msg02").css("display","none");
			zc_box_main_msg=false;
		}else{
			$(".zc_box_main_msg01").css("display","none");
			$(".zc_box_main_msg02").css("display","inline-block");
			zc_box_main_msg=true;
		}
	});
	
	$(".tan_bj").click(function(){
		$(".zc_box,.wj_box").fadeOut();
		$(this).fadeOut();
	});
	
	$(".login_box_main_zcbt").click(function(){
		$(".zc_box,.tan_bj").fadeIn();
	});


	$(".login_box_main_msgr").click(function(){
		$(".wj_box,.tan_bj").fadeIn();
	});	
	
	$(".a0_l p").click(function(){
		$(".a0_l p span").removeClass("active");
		$(this).children("span").addClass("active");
	});	
	
	
	$(".pm_se p").click(function(){
		$(".pm_se p i").removeClass("active");
		$(this).children("i").addClass("active");
	});	
	





	
	
	 
	
});