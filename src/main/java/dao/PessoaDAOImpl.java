package dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import model.Pessoa;
import dao.Interface.PessoaDAO;

public class PessoaDAOImpl extends HibernateGenericDAO<Pessoa, Long> implements PessoaDAO, Serializable {

	private static final long serialVersionUID = 1L;

	public List<Pessoa> buscarTodas(){
		List<Pessoa> listaPessoas;
		try {
			listaPessoas = (ArrayList<Pessoa>) em.createQuery("FROM Pessoa", Pessoa.class)
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return listaPessoas;
	}

	public Pessoa buscarPorNomePessoa(String variavel) {
		Pessoa pessoa;
		try {
			pessoa =  em.createQuery("FROM Pessoa WHERE nome = :codigo", Pessoa.class)
					.setParameter("codigo", variavel)
					.getSingleResult();

		}  catch(NoResultException e){
			return null;
		}
		return pessoa;
	}

	public Pessoa buscarPorSiapePessoa(String variavel) {
		Pessoa pessoa;
		try {
			pessoa =  em.createQuery("FROM Pessoa WHERE siape = :codigo", Pessoa.class)
					.setParameter("codigo", variavel)
					.getSingleResult();
		}  catch(NoResultException e){
			return null;
		}
		return pessoa;
	}	

	public Pessoa alterarSenha(Pessoa pessoaSelecionada) {
		Pessoa pessoa = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("UPDATE Pessoa SET SENHA = :senha WHERE id = :codigo"); 
			query.setParameter("codigo", pessoaSelecionada.getId());
			query.setParameter("senha", pessoaSelecionada.getSenha());
			query.executeUpdate();
			em.getTransaction().commit();
		}  catch(NoResultException e){
			return null;
		}
		return pessoa;
	}
	public void resetDAO(){

		em.clear();
	}
}