$( document ).ready(function() {
		
    $("#numeroId").bind('change', function (event){
    	var numeroId = $("#numeroId").val() ; 
    	var typeId = $("#typeId").val();    	
    	alert(typeId+'-'+numeroId) ;
    	var url = '/webapi/visite/typeId/'+typeId+'/numeroId/'+numeroId ;
    	alert(url) ;
    	$.ajax({url: url, 
    			type: 'GET',
    			success: function(result){
    				var result_data ;
    				if( Array.isArray(result)){
    					result_data = result[result.length-1] ;
    				}else {
    					result_data = result;
    				}
    				$("#nom").val(result_data.nom) ; 
    				$("#datePi").val(result_data.datePi) ;
    				$("#qualite").val(result_data.qualite) ;
    				$("#numeroWhatsapp").val(result_data.numeroWhatsapp) ;
    				$("#email").val(result_data.email) ;
    				$("#motif").val(result_data.motif).prop('selected', true) ;
    				$("#pays").val(result_data.pays.id) ;
    	 		},
    	 		failure: function(result){
    				$("#nom").val('') ; 
    				$("#datePi").val('') ;
    				$("#qualite").val('') ;
    				$("#numeroWhatsapp").val('') ;
    				$("#email").val('') ;
    	 		}
    		});
     
    } );
    
    $("#typeId").bind('change', function (event){
    	var numeroId = $("#numeroId").val() ; 
    	var typeId = $("#typeId").val();    	
    	alert(typeId+'-'+numeroId) ;
    	var url = '/webapi/visite/typeId/'+typeId+'/numeroId/'+numeroId ;
    	type: 'GET',
    	$.ajax({url: url, 
    		       data :{format:'json'}, 
    			   success: function(result){
    				if(result == true) {
    					alert(result);
    				}else {
    					alert('None') ;
    				}
    	 		}
    		}
    	);
     
    } );

});

