package controller.backend;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Named
public class LoginController {
	
	@Inject
	private HttpServletRequest request;
	
	public void onPageLoad()
	{
		//Invalida a sessão do usuário e evita que ele refaça o login
		HttpSession session = request.getSession();
		session.invalidate();
	}

}
