package br.ufjf.coordenacao.sistemagestaocurso.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import br.ufjf.coordenacao.sistemagestaocurso.model.Disciplina;

public class DisciplinaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	public DisciplinaRepository() 	{ }
	
	public DisciplinaRepository(EntityManager manager)
	{
		this.manager = manager;
	}
	
	public Disciplina porid(long id) {
		return manager.find(Disciplina.class, id);
	}
	
	public Disciplina persistir(Disciplina objeto) {
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

	public void remover(Disciplina objeto) {
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

	public List<Disciplina> listarTodos() {
		return manager.createQuery("FROM Disciplina", Disciplina.class).getResultList();
	}

	public List<String> buscarTodosCodigosDisciplina(String variavel) {
		return manager
				.createQuery("Select codigo FROM Disciplina WHERE codigo like :codigo order by codigo", String.class)
				.setParameter("codigo", "%" + variavel + "%").getResultList();

	}

	public Disciplina buscarPorCodigoDisciplina(String variavel) {
		try {
			return manager.createQuery("FROM Disciplina WHERE codigo = :codigo", Disciplina.class)
					.setParameter("codigo", variavel).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<String> buscarTodosNomesDisciplina(String variavel) {
		return manager.createQuery("Select nome FROM Disciplina WHERE nome like :codigo", String.class)
				.setParameter("codigo", "%" + variavel + "%").getResultList();
	}

	public List<Disciplina> buscarTodosNomesDisciplinaObjeto(String variavel) {
		return manager.createQuery("FROM Disciplina WHERE nome like :codigo", Disciplina.class)
				.setParameter("codigo", "%" + variavel + "%").getResultList();
	}

	public Disciplina buscarPorNomeDisciplina(String variavel) {
		try {
			return manager.createQuery("FROM Disciplina WHERE nome = :codigo", Disciplina.class)
					.setParameter("codigo", variavel).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
