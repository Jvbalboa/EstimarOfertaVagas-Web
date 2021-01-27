package br.ufjf.coordenacao.sistemagestaocurso.repository;

import br.ufjf.coordenacao.sistemagestaocurso.model.Pessoa;

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
	
	public Pessoa buscarPorSiapePessoa2(String variavel) {
		EntityTransaction transaction = null;
		
		Pessoa p = null;
		try {
			transaction = manager.getTransaction();
			if (!transaction.isActive())
				transaction.begin();
			p = (Pessoa) manager.createNativeQuery("SELECT * FROM Pessoa WHERE siape = :codigo")
					.setParameter("codigo", variavel).getSingleResult();
			transaction.commit();
		} catch (NoResultException e) {
			transaction.rollback();
			return null;
		}
		
		return p;

	}

	public int alterarSenha(Pessoa pessoaSelecionada) {
		EntityTransaction transaction = null;
		
		int alterou = 0;
		try {
			transaction = manager.getTransaction();
			if (!transaction.isActive())
				transaction.begin();

			alterou = manager.createQuery("UPDATE Pessoa SET SENHA = :senha WHERE id = :codigo")
					.setParameter("codigo", pessoaSelecionada.getId()).setParameter("senha", pessoaSelecionada.getSenha())
					.executeUpdate();
			
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
		
		return alterou;
	}
}