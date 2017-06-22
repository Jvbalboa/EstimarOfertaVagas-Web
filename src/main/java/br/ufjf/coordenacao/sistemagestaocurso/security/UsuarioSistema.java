package br.ufjf.coordenacao.sistemagestaocurso.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.Autenticacao;

public class UsuarioSistema extends User {

	private static final long serialVersionUID = 1L;
	private Autenticacao usuario;

	public UsuarioSistema(Autenticacao usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getLogin(), "123"/*usuario.getSenha()*/, authorities);
		this.usuario = usuario;
		// TODO Auto-generated constructor stub
	}

	public Autenticacao getUsuario()
	{
		return this.usuario;
	}
}
