package br.ufjf.coordenacao.sistemagestaocurso.controller.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.ufjf.coordenacao.sistemagestaocurso.model.Pessoa;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.Autenticacao;
import br.ufjf.coordenacao.sistemagestaocurso.repository.PessoaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.EstruturaArvore;
import br.ufjf.ice.integra3.ws.login.IWsLogin;
import br.ufjf.ice.integra3.ws.login.WsLoginResponse;
import br.ufjf.ice.integra3.ws.login.WsUserInfoResponse;
import br.ufjf.ice.integra3.ws.login.WSLogin;

@Named
public class AutenticacaoController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AutenticacaoController.class);
	
	@Inject
	private PessoaRepository pessoaDAO;
	
	public Autenticacao logar(Autenticacao autenticacao) throws IOException {


		EstruturaArvore estruturaArvore;
		estruturaArvore = EstruturaArvore.getInstance();
		final String tokenPath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("tokenPath");

		List<String> perfis = new ArrayList<String>();
		FileReader file = new FileReader(tokenPath);
		BufferedReader bf = new BufferedReader(file);		
		String token = bf.readLine();
		bf.close();
		file.close();

		try {
			logger.info("Logando " + autenticacao.getLogin());
			IWsLogin integra = new WSLogin().getWsLoginServicePort();
			WsLoginResponse user = integra.login(autenticacao.getLogin(), autenticacao.getSenha(), token);
			WsUserInfoResponse infos = integra.getUserInformation(user.getToken()); // Pegando informa��es			
			int contador;
			boolean achouCoord = false;
			Pessoa pessoa = null;

			for (contador = 0;contador < infos.getProfileList().getProfile().size() ; contador ++ ){				
				
				Pessoa pessoaTemp = pessoaDAO.buscarPorSiapePessoa(infos.getProfileList().getProfile().get(contador).getMatricula());
				
				perfis.add(infos.getProfileList().getProfile().get(contador).getMatricula());			
				if (pessoaTemp != null){				
					achouCoord = true;
					pessoa = pessoaTemp;
				}				
			}	

			if (achouCoord == true){

				autenticacao.setPessoa(pessoa);
				autenticacao.setToken(token);
				autenticacao.setTipoAcesso("coordenador");
				autenticacao.setMaiorPermissao("coordenador");
				estruturaArvore.setLoginUtilizado("coordenador");	
				autenticacao.setPerfis(perfis);
				return autenticacao;

			}

			autenticacao.setPerfis(perfis);
			autenticacao.setTipoAcesso("aluno");
			autenticacao.setMaiorPermissao("aluno");
			estruturaArvore.setLoginUtilizado("aluno");
			return autenticacao;

		} 
		catch (Exception e) {
			//Caso as credenciais sejam inválidas, o webservice lançará um exceção
			if(!e.getMessage().contains("Usuário ou senha não cadastrados"))
			{
				logger.error("Erro ao realizar o login pelo SIGA " + autenticacao.getLogin(), e);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ocorreu um problema", e.getMessage()));
			}
		}

		try {
			List<Pessoa> TodasPessoas = (List<Pessoa>) pessoaDAO.listarTodos();
			for (Pessoa pessoaComparada : TodasPessoas) {
				if (pessoaComparada.getSenha().equals(autenticacao.getSenha())
						&& pessoaComparada.getSiape().equals(autenticacao.getLogin())) {
					autenticacao.setPessoa(pessoaComparada);
					autenticacao.setTipoAcesso("externo");
					estruturaArvore.setLoginUtilizado("externo");
					autenticacao.setMaiorPermissao("externo");

					if (pessoaComparada.getSiape().equals("admin")) {
						autenticacao.setTipoAcesso("admin");
						estruturaArvore.setLoginUtilizado("admin");
						autenticacao.setMaiorPermissao("admin");

					}

					return autenticacao;
				}
			}
		} catch (Exception e) {
			logger.error("Erro ao realizar o login local", e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ocorreu um problema", e.getMessage()));
		}
		autenticacao.setTipoAcesso("acessoNegado");
		return autenticacao;

	}
}
