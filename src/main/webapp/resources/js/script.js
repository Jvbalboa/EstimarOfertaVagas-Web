 
/* Requisição de Folga 
 * Alterado por: Charleston Campos
 * Data: 24/07/2014 */


$(function() {
	$(PrimeFaces.escapeClientId('TrabExtraForm:dataInicial')).datetimepicker({
		formatDate:'d/m/Y H:i',
		minDate:'+1970/12/01',
		
  
    	onShow:function( ct ){
		   this.setOptions({
			   maxDate:$(PrimeFaces.escapeClientId('TrabExtraForm:dataFinal')).val()?
					   $(PrimeFaces.escapeClientId('TrabExtraForm:dataFinal')).val():false
		   });
    	},    	

    	/*weekends:['01.01.2014','02.01.2014','03.01.2014','04.01.2014','05.01.2014','06.01.2014'],
    	timepicker:true,*/
    	step:30,
    	allowTimes:[
    		'00:00', '6:00', '6:30', '7:00', '7:30', '08:00','08:30','9:00','9:30',
    		'10:00','10:30','11:00','11:30','12:00','12:30','13:00','13:30',
    		'14:00','14:30','15:00','15:30','16:00','16:30','17:00','17:30',
    		'18:00', '18:30','19:00','19:30','20:00','20:30','21:00','23:59']
    });
    	
       
	$(PrimeFaces.escapeClientId('TrabExtraForm:dataFinal')).datetimepicker({
		formatDate:'d/m/Y H:i',
		
   	
    	onShow:function( ct ){
		   this.setOptions({
			minDate:$(PrimeFaces.escapeClientId('TrabExtraForm:dataInicial')).val()?
		    		$(PrimeFaces.escapeClientId('TrabExtraForm:dataInicial')).val():false
		   });
    	},
    	
    	/*weekends:['01.01.2014','02.01.2014','03.01.2014','04.01.2014','05.01.2014','06.01.2014'],
    	timepicker:true,*/
    	step:30,
    	allowTimes:[
    		'00:00','6:00', '6:30', '7:00', '7:30', '08:00','08:30','9:00','9:30',
    		'10:00','10:30','11:00','11:30','12:00','12:30','13:00','13:30',
    		'14:00','14:30','15:00','15:30','16:00','16:30','17:00','17:30',
    		'18:00', '18:30','19:00','19:30','20:00','20:30','21:00', '23:59']
    	});
    		    
 });

$(function() {
	$(PrimeFaces.escapeClientId('FolgaForm:dataInicial')).datetimepicker({
		formatDate:'d/m/Y H:i',
		minDate:'+1970/12/01',
		
    	onGenerate:function( ct ){
		    $(this).find('.xdsoft_date.xdsoft_weekend')
		      .addClass('xdsoft_disabled');
    	},
    	onShow:function( ct ){
		   this.setOptions({
			   maxDate:$(PrimeFaces.escapeClientId('FolgaForm:dataFinal')).val()?
					   $(PrimeFaces.escapeClientId('FolgaForm:dataFinal')).val():false
		   });
    	},    	

    	/*weekends:['01.01.2014','02.01.2014','03.01.2014','04.01.2014','05.01.2014','06.01.2014'],
    	timepicker:true,*/
    	step:30,
    	allowTimes:[
    		'00:00', '6:00', '6:30', '7:00', '7:30', '08:00','08:30','9:00','9:30',
    		'10:00','10:30','11:00','11:30','12:00','12:30','13:00','13:30',
    		'14:00','14:30','15:00','15:30','16:00','16:30','17:00','17:30',
    		'18:00', '18:30','19:00','19:30','20:00','20:30','21:00','23:59']
    });
    	
       
	$(PrimeFaces.escapeClientId('FolgaForm:dataFinal')).datetimepicker({
		formatDate:'d/m/Y H:i',
		
    	onGenerate:function( ct ){
		    $(this).find('.xdsoft_date.xdsoft_weekend')
		      .addClass('xdsoft_disabled');
    	},
    	
    	onShow:function( ct ){
		   this.setOptions({
			minDate:$(PrimeFaces.escapeClientId('FolgaForm:dataInicial')).val()?
		    		$(PrimeFaces.escapeClientId('FolgaForm:dataInicial')).val():false
		   });
    	},
    	
    	/*weekends:['01.01.2014','02.01.2014','03.01.2014','04.01.2014','05.01.2014','06.01.2014'],
    	timepicker:true,*/
    	step:30,
    	allowTimes:[
    		'00:00','6:00', '6:30', '7:00', '7:30', '08:00','08:30','9:00','9:30',
    		'10:00','10:30','11:00','11:30','12:00','12:30','13:00','13:30',
    		'14:00','14:30','15:00','15:30','16:00','16:30','17:00','17:30',
    		'18:00', '18:30','19:00','19:30','20:00','20:30','21:00', '23:59']
    	});
    		    
 });




$(function() {
	$(PrimeFaces.escapeClientId('FolgaFormRelatorio:dataInicial')).datetimepicker({
		format:'d/m/Y',
		
    	onGenerate:function( ct ){
		    $(this).find('.xdsoft_date.xdsoft_weekend')
		      .addClass('xdsoft_disabled');
    	},
    	
    	onShow:function( ct ){
 		   this.setOptions({
 			   maxDate:$(PrimeFaces.escapeClientId('FolgaFormRelatorio:dataFinal')).val()?
 					   $(PrimeFaces.escapeClientId('FolgaFormRelatorio:dataFinal')).val():false
 		   });
     	},    	
    	
    	/*weekends:['01.01.2014','02.01.2014','03.01.2014','04.01.2014','05.01.2014','06.01.2014'],
    	timepicker:false,*/
    	});
	
	$(PrimeFaces.escapeClientId('FolgaFormRelatorio:dataFinal')).datetimepicker({
		format:'d/m/Y',
		
    	onGenerate:function( ct ){
		    $(this).find('.xdsoft_date.xdsoft_weekend')
		      .addClass('xdsoft_disabled');
    	},
    	
    	onShow:function( ct ){
 		   this.setOptions({
 			minDate:$(PrimeFaces.escapeClientId('FolgaFormRelatorio:dataInicial')).val()?
 		    		$(PrimeFaces.escapeClientId('FolgaFormRelatorio:dataInicial')).val():false
 		   });
     	},
    	
    	/*weekends:['01.01.2014','02.01.2014','03.01.2014','04.01.2014','05.01.2014','06.01.2014'],
    	timepicker:false,
    	*/});
	
 });


$(function() {
	$(PrimeFaces.escapeClientId('FolgaFormGrafico:dataInicial')).datetimepicker({
		formatDate:'d/m/Y H:i',
		
		
    	onGenerate:function( ct ){
		    $(this).find('.xdsoft_date.xdsoft_weekend')
		      .addClass('xdsoft_disabled');
    	},
    	onShow:function( ct ){
		   this.setOptions({
			   maxDate:$(PrimeFaces.escapeClientId('FolgaFormGrafico:dataFinal')).val()?
					   $(PrimeFaces.escapeClientId('FolgaFormGrafico:dataFinal')).val():false
		   });
    	},    	

    	/*weekends:['01.01.2014','02.01.2014','03.01.2014','04.01.2014','05.01.2014','06.01.2014'],
    	timepicker:true,*/
    	step:30,
    	allowTimes:[
    		'00:00', '6:00', '6:30', '7:00', '7:30', '08:00','08:30','9:00','9:30',
    		'10:00','10:30','11:00','11:30','12:00','12:30','13:00','13:30',
    		'14:00','14:30','15:00','15:30','16:00','16:30','17:00','17:30',
    		'18:00', '18:30','19:00','19:30','20:00','20:30','21:00','23:59']
    });
    	
       
	$(PrimeFaces.escapeClientId('FolgaFormGrafico:dataFinal')).datetimepicker({
		formatDate:'d/m/Y H:i',
		
    	onGenerate:function( ct ){
		    $(this).find('.xdsoft_date.xdsoft_weekend')
		      .addClass('xdsoft_disabled');
    	},
    	
    	onShow:function( ct ){
		   this.setOptions({
			minDate:$(PrimeFaces.escapeClientId('FolgaFormGrafico:dataInicial')).val()?
		    		$(PrimeFaces.escapeClientId('FolgaFormGrafico:dataInicial')).val():false
		   });
    	},
    	
    	/*weekends:['01.01.2014','02.01.2014','03.01.2014','04.01.2014','05.01.2014','06.01.2014'],*/timepicker:true,step:30,
    	allowTimes:[
    		'00:00','6:00', '6:30', '7:00', '7:30', '08:00','08:30','9:00','9:30',
    		'10:00','10:30','11:00','11:30','12:00','12:30','13:00','13:30',
    		'14:00','14:30','15:00','15:30','16:00','16:30','17:00','17:30',
    		'18:00', '18:30','19:00','19:30','20:00','20:30','21:00', '23:59']
    	});
    		    
 });






