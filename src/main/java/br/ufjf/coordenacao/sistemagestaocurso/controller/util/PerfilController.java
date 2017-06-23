package br.ufjf.coordenacao.sistemagestaocurso.controller.util;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;



@Named
@SessionScoped 
public class PerfilController implements Serializable {

	private static final long serialVersionUID = 1L;

	//========================================================= VARIABLES ==================================================================================//

	private String perfil;

	//========================================================= METODOS ==================================================================================//

	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init() {
		
		if(usuarioController.getAutenticacao().getTipoAcesso() == null){
			return;
		}

		
			if (usuarioController.getAutenticacao().getLogin().equals("admin")){		
				perfil = "Templete.xhtml";
			}
			else if (usuarioController.getAutenticacao().getTipoAcesso().equals("aluno")){
				perfil = "TempleteAluno.xhtml";
			}
			else if (usuarioController.getAutenticacao().getTipoAcesso().equals("externo")){
				perfil = "TempleteExterno.xhtml";
			}
			else {
				perfil = "TempleteCoordenador.xhtml";
			}
		
	}

	public String getPerfil() {
		init();
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}	
}