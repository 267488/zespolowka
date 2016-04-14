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
	
	
	function drawDonut(elem){
	  var node = document.querySelector(elem);
	  var width = height = node.getAttribute("data-size") || 204;
	  var thickness = node.getAttribute("data-thickness") || 26;
	  var duration = node.getAttribute('data-duration') || 850;
	  var delay = node.getAttribute('data-delay') || 100;
	  var amounts = node.getAttribute("data-amounts").split(",");
	  var fills = node.getAttribute("data-fills").split(",");
	  
	  var radius = Math.min(width, height) / 2;
	  var pie = d3.layout.pie().sort(null);
	  
	  var svg = d3.select(elem).append("svg")
		  .attr("width", width)
		  .attr("height", height)
		  .append("g")
		  .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")")
		
	  var arc = d3.svg.arc()
		  .innerRadius(radius - thickness)
		  .outerRadius(radius);
	  
	  svg.selectAll("path")
		.data(pie(amounts))
		.enter()
		.append("path")
		.style("fill", function(d, i) { return fills[i]; })
		.attr("d", arc)
		.transition()
		.delay(delay)
		.duration(duration)
		.call(arcTween);
	  
	  function arcTween(transition) {
		transition.attrTween("d", function(d) {
		  var interpolate = d3.interpolate(d.startAngle, d.endAngle);
		  return function(t) {
			d.endAngle = interpolate(t);
			return arc(d);
		  };
		});
	  }
	}
	
	
	drawDonut("#graph0");
	drawDonut("#graph1");
});
