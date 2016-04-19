$( document ).ready(function() {

	$(".graph.legend").each(function() {
		var counter = 1;
		$("li", this).each(function() {
			$("span", this).addClass( "color"+counter );
			counter++;
		});
		if( counter > 4 ) {
			$(this).addClass("two-rows");
		}	
	}); 
	
	var width = $(window).width();
	if( width > 1199 ) {
		$(".list-container.list1").mCustomScrollbar({
			scrollInertia: 250,
			mouseWheel:{ scrollAmount: 64 }
		});
                $(".list-container.list2").mCustomScrollbar({
			scrollInertia: 250,
			mouseWheel:{ scrollAmount: 64 }
		});
	}
	
});
