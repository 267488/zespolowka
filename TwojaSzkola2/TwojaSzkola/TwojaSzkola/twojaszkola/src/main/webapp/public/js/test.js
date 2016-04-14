$(function() {
	$('.list-container > .content > .text-container > button.star').click(function(){
		var id = $(this).css("id");
		alert('Dodano do ulubionych szkołę o id = '+id);
	});
});
