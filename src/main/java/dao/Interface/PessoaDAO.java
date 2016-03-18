package dao.Interface;



import java.util.List;

import model.Pessoa;

public interface PessoaDAO extends GenericDAO<Pessoa, Long> {
	public Pessoa buscarPorNomePessoa(String variavel);
	public Pessoa buscarPorSiapePessoa(String variavel);
	public List<Pessoa> buscarTodas();
	public Pessoa alterarSenha(Pessoa pessoaSelecionada) ;
	public void resetDAO();
}