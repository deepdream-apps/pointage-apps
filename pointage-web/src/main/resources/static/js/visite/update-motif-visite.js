$(document).ready(function() {
    $("#motif").change(function (event){
    	$('#employeDIV').hide() ;
    	$('#evenementDIV').show() ;
    	var motif = $('#motif').val() ;
    	if(motif == '1') {
    		$('#employeDIV').hide() ;
    		$('#evenementDIV').show() ;
    		$('#prestationsDIV').hide() ;
    	}else if (motif == '2'){
    		$('#employeDIV').show() ;
    		$('#evenementDIV').hide() ;
    		$('#prestationsDIV').hide() ;
    	}else if (motif == '3'){
    		$('#employeDIV').hide() ;
    		$('#evenementDIV').hide() ;
    		$('#prestationsDIV').show() ;
    	}
     
    } );

});

