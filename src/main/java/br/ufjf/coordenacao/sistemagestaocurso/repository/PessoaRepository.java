package br.ufjf.coordenacao.sistemagestaocurso.repository;

import br.ufjf.coordenacao.sistemagestaocurso.model.Pessoa;
import br.ufjf.coordenacao.sistemagestaocurso.util.jpa.EntityManagerProducer;

import java.util.List;
import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public class PessoaRepository implements Serializable {

	@Inject
	private EntityManager manager;
	
	private static final long serialVersionUID = 1L;

	public Pessoa porid(long id) {
		return manager.find(Pessoa.class, id);
	}

	public Pessoa persistir(Pessoa objeto) {
		EntityTransaction transaction = null;
		
		try {
			transaction = manager.getTransaction();
			transaction.begin();
			objeto = manager.merge(objeto);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
		
		return objeto;
	}

	public void remover(Pessoa objeto) {
		EntityTransaction transaction = null;
		
		try {
			transaction = manager.getTransaction();
			transaction.begin();
			manager.remove(manager.contains(objeto) ? objeto : manager.merge(objeto));
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<Pessoa> listarTodos() {
		return manager.createQuery("FROM Pessoa", Pessoa.class).getResultList();
	}

	public Pessoa buscarPorNomePessoa(String variavel) {
		try {
			return manager.createQuery("FROM Pessoa WHERE nome = :codigo", Pessoa.class)
					.setParameter("codigo", variavel).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public Pessoa buscarPorSiapePessoa(String variavel) {
		try {
			return manager.createQuery("FROM Pessoa WHERE siape = :codigo", Pessoa.class)
					.setParameter("codigo", variavel).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public int alterarSenha(Pessoa pessoaSelecionada) {

		return manager.createQuery("UPDATE Pessoa SET SENHA = :senha WHERE id = :codigo")
				.setParameter("codigo", pessoaSelecionada.getId()).setParameter("senha", pessoaSelecionada.getSenha())
				.executeUpdate();

	}
}