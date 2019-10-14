package br.ufjf.coordenacao.sistemagestaocurso.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.util.jpa.EntityManagerProducer;

public class CursoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;


	public Curso porid(long id) {
		return manager.find(Curso.class, id);
	}

	public Curso persistir(Curso curso) {
		EntityTransaction transaction = null;
		
		try {
			transaction = manager.getTransaction();
			transaction.begin();
			curso = manager.merge(curso);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
		
		return curso;
	}

	public int removerTodosAlunos(Curso curso)	{
		return manager.createQuery("DELETE FROM Aluno WHERE ID_CURSO = :curso")
				.setParameter("curso", curso.getId())
				.executeUpdate();
	}
	
	public void remover(Curso curso) {
		EntityTransaction transaction = null;
		
		try {
			transaction = manager.getTransaction();
			transaction.begin();
			manager.remove(manager.contains(curso) ? curso : manager.merge(curso));
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<Curso> listarTodos() {
		return manager.createQuery("FROM Curso", Curso.class).getResultList();
	}

	public Curso buscarPorCodigo(String variavel) {
		try {
			return manager.createQuery("FROM Curso WHERE codigo = :codigo", Curso.class)
					.setParameter("codigo", variavel).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Curso buscarPorNome(String variavel) {
		try {
			return manager.createQuery("FROM Curso WHERE nome = :codigo", Curso.class).setParameter("codigo", variavel)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<String> buscarTodosCodigosCurso(String variavel) {
		return manager.createQuery("Select codigo FROM Curso WHERE codigo like :codigo", String.class)
				.setParameter("codigo", "%" + variavel + "%").getResultList();
	}
	
	public List<String> buscarTodosCodigosCurso() {
		return manager.createQuery("Select codigo FROM Curso", String.class).getResultList();
	}

	public List<String> buscarTodosNomesCurso(String variavel) {
		return manager.createQuery("Select nome FROM Curso WHERE nome like :codigo", String.class)
				.setParameter("codigo", "%" + variavel + "%").getResultList();

	}

	public List<Curso> buscarTodosNomesObjetoCurso(String variavel) {
		return manager.createQuery("FROM Curso WHERE nome like :codigo", Curso.class)
				.setParameter("codigo", "%" + variavel + "%").getResultList();

	}
}
