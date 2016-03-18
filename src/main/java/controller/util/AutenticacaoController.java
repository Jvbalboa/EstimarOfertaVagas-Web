package controller.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotAuthorizedException;

import br.ufjf.ice.integra3.ws.login.interfaces.IWsLogin;
import br.ufjf.ice.integra3.ws.login.interfaces.WsException_Exception;
import br.ufjf.ice.integra3.ws.login.interfaces.WsLoginResponse;
import br.ufjf.ice.integra3.ws.login.interfaces.WsUserInfoResponse;
import br.ufjf.ice.integra3.ws.login.service.WSLogin;
import model.Pessoa;
import model.estrutura.Autenticacao;
import dao.Interface.PessoaDAO;


public class AutenticacaoController {

	public static Autenticacao logar(Autenticacao autenticacao) throws IOException {

		EstruturaArvore estruturaArvore;
		estruturaArvore = EstruturaArvore.getInstance();
		estruturaArvore.resetarPessoaDAO();
		PessoaDAO pessoaDAO = estruturaArvore.getPessoaDAO();
		List<String> perfis = new ArrayList<String>();		
		FileReader file = new FileReader("/opt/application.token");
		BufferedReader bf = new BufferedReader(file);		
		String token = bf.readLine();
		bf.close();
		file.close();

		try {
			System.out.println("Logando...");
			IWsLogin integra = new WSLogin().getWsLoginServicePort();
			WsLoginResponse user = integra.login(autenticacao.getLogin(), autenticacao.getSenha(), token);
			WsUserInfoResponse infos = integra.getUserInformation(user.getToken()); // Pegando informa��es			
			int contador;
			boolean achouCoord = false;
			Pessoa pessoa = null;

			for (contador = 0;contador < infos.getProfileList().getProfile().size() ; contador ++ ){				
				pessoa = pessoaDAO.buscarPorSiapePessoa(infos.getProfileList().getProfile().get(contador).getMatricula());				
				perfis.add(infos.getProfileList().getProfile().get(contador).getMatricula());			
				if (pessoa != null){				
					achouCoord = true;												
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

		} catch (NotAuthorizedException e) {
		} catch (WsException_Exception e) {
		} catch (Exception e) {
		}



		/*************************************************************************************************/

			/*int contador;
		List<String> listaPeris = new ArrayList<String>();

		listaPeris.add("200951711");
		listaPeris.add("200976006");
		listaPeris.add("1146429");

		boolean achouCoord = false;
		Pessoa pessoa = null;

		for (contador = 0;contador < listaPeris.size() ; contador ++ ){				
			pessoa = pessoaDAO.buscarPorSiapePessoa(listaPeris.get(contador));				
			perfis.add(listaPeris.get(contador));			
			if (pessoa != null){				
				achouCoord = true;												
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
		return autenticacao;	*/


		/*******************************************************************************************************/


		List<Pessoa> TodasPessoas =  (List<Pessoa>) pessoaDAO.recuperarTodos();
		for (Pessoa pessoaComparada:TodasPessoas){
			if(pessoaComparada.getSenha().equals(autenticacao.getSenha()) && pessoaComparada.getSiape().equals(autenticacao.getLogin())){
				autenticacao.setPessoa(pessoaComparada);
				autenticacao.setTipoAcesso("externo");
				estruturaArvore.setLoginUtilizado("externo");
				autenticacao.setMaiorPermissao("externo");

				if (pessoaComparada.getSiape().equals("admin")){
					autenticacao.setTipoAcesso("admin");
					estruturaArvore.setLoginUtilizado("admin");
					autenticacao.setMaiorPermissao("admin");

				}


				return autenticacao;
			}
		}
		autenticacao.setTipoAcesso("acessoNegado");
		return autenticacao;
	}
}