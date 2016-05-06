$( document ).ready(function() {

	var sliders = $("#sliders .slider");
    var smax = 100;
    var smin = 0;


	sliders.each(function() {
		var tempMaxValue = 0;
		var startValue = parseInt( $(this).siblings(".value"). text(), 10 );
		var totalValue = $("#total").text();
		$("#total").html( totalValue - startValue );

		$(this).empty().slider({
			value: startValue,
			min: smin,
			max: smax,
			range: 'min',
			step: 1,
			slide: function(event, ui) {
				
				$(this).siblings(".value").text(ui.value);

				var total = 0;
				
				sliders.not(this).each(function() {
					total += $(this).slider("option", "value");
				});
				
				total += ui.value;
	   
				if(total == smax) {
					tempMaxValue = ui.value;
				}
				if(total > smax) {
				  ui.value = tempMaxValue;
				  $(this).siblings(".value").text(ui.value);
				  return false;
				}
				
				$("#total").html(smax-total);
			}
		});
	
	});
/*
   sliders.each(function() {
       var v = parseInt( $(this).siblings(".value"). text(), 10 );
			$(this).slider( "option", "value", v);
	 });*/
	
	$("#undoIcon").on("click", function(){
		sliders.each(function() {
			$("#total").html("100");
			$(this).siblings(".value").text(0);
			$(this).find(".ui-slider-range").animate( {"width": "0%"}, 400);
			$(this).find(".ui-slider-handle").animate( {"left": "0%"}, 400);
			
			setTimeout(function(){
				sliders.each(function() {
					$(this).slider( "option", "value", 0 );
				});
			}, 400);
		});
	});
	
});
