package springSecurity;
import java.util.Collection;

import model.estrutura.Autenticacao;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;



public class UsuarioSistema extends User {

	private static final long serialVersionUID = 1L;
	
	public UsuarioSistema(Autenticacao usuario, Collection<? extends GrantedAuthority> authorities) {		
		super(usuario.getLogin(), "123", authorities);		
	}
}