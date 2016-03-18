package springSecurity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.estrutura.Autenticacao;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import controller.util.EstruturaArvore;

public class AppUserDetailsService implements UserDetailsService {
	public UserDetails loadUserByUsername(String UsuarioRede)throws UsernameNotFoundException {
		Autenticacao usuario = new  Autenticacao();
		usuario.setLogin(UsuarioRede);
		UsuarioSistema user = null;
		if (usuario != null) {
			user = new UsuarioSistema(usuario, getGrupos(usuario));
		}
		return user;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Autenticacao usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		EstruturaArvore estruturaArvore;
		estruturaArvore = EstruturaArvore.getInstance();
		if (usuario.getLogin().toUpperCase().equals("ADMIN") ){
			authorities.add(new SimpleGrantedAuthority("admin"));
		}
		else if(estruturaArvore.getLoginUtilizado().equals("coordenador")){
			authorities.add(new SimpleGrantedAuthority("coordenador"));
			}
		else if (estruturaArvore.getLoginUtilizado().equals("externo")){
			authorities.add(new SimpleGrantedAuthority("externo"));
		}else {
			authorities.add(new SimpleGrantedAuthority("aluno"));
		}
		return authorities;
	}	
}