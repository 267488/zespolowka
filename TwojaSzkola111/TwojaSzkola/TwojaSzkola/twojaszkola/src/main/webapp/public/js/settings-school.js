$( document ).ready(function() {

	/*
	$('#editProfile').on('show.bs.modal', function (event) {
		var modal = $(this);
		var button = $(event.relatedTarget);
		var recipient = button.data('whatedit');
		
		var profileID = recipient.substring(8);

		modal.find('.modal-body input#profileName').val('nazwa');
		modal.find('.modal-body input#profilePoints').val(2);
		modal.find('.modal-body input#profileSpots').val(3);
		
		var profileSubjects = [ 'Matematyka', 'Angielski' ];
		
		$.each( profileSubjects, function( i, val) {
			
			alert('Each: '+i);
			var newVal = val.toLowerCase();
		
			$( ".modal#editProfile .checkbox-container > .checkbox > label > span" ).each( function() {
				var checkboxText = $(this).text().toLowerCase();
				alert('checkboxText='+checkboxText+' $(this)='+$(this).text());
				
				if( newVal == checkboxText ) {
					$(this).prop('checked', true);
					return false;
				}
			});
		
		});
	});
	*/
	
	$(".widget.student-settings > ul > li").click(function() {
		var className = $(this).attr("class");
		
		if( className.indexOf("tab1") >= 0 ) {
			$(".widget.student-settings > .btn.addNew").attr("data-target", "#addNewProfile");
		}
		if( className.indexOf("tab2") >= 0 ) {
			$(".widget.student-settings > .btn.addNew").attr("data-target", "#addNewClub");
		}
		if( className.indexOf("tab3") >= 0 ) {
			$(".widget.student-settings > .btn.addNew").attr("data-target", "#addNewAchievement");
		}
	});
	
	
	
	
});
