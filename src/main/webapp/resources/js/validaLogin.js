function loadValidate() {
	$("#formularioLogin").validate({
		rules:{
			'formularioLogin:colaborador':{
				required: true, minlength: 5, maxlength: 40
			},
			'formularioLogin:senha':{
				required: true, minlength: 4, maxlength: 40
			},
		},
		messages:{
			'formularioLogin:colaborador':{
				required: "Digite o seu usuário de rede",
				minlength: "O seu usuário de rede deve conter, no mínimo, 5 caracteres",
				maxlength: "O seu usuário de rede deve conter, no máximo, 40 caracteres"
			},
			'formularioLogin:senha':{
				required: "Digite a sua senha de rede",
				minlength: "A sua senha de rede deve conter, no mínimo, 5 caracteres",
				maxlength: "A sua senha de rede deve conter, no máximo, 40 caracteres"
			},
		}
	});	
}
	
function validateForm(){    
      
    if(!$("#formularioLogin").validate().form())  
        return false;  
      
    return true;  
}

function desabilitaTeclas(e){
	
	var tecla=new Number();
	if(window.event) {
		tecla = e.keyCode;
	}
	else if(e.which) {
		tecla = e.which;
	}
	else {
		return true;
	}
	if((tecla >= "47") && (tecla <= "64")){
		return false;
	}	
	if((tecla >= "33") && (tecla <= "45")){
		return false;
	}
	if((tecla >= "91") && (tecla <= "96")){
		return false;
	}
	if((tecla >= "123") && (tecla <= "248")){
		return false;
	}

}
	