package br.ufjf.coordenacao.sistemagestaocurso.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
public class LoginController {
	
	@Inject
	private HttpServletRequest request;
	
	public void onPageLoad()
	{
		//Invalida a sesso do usurio e evita que ele refaa o login
//		HttpSession session = request.getSession();
//		session.invalidate();
	}

}