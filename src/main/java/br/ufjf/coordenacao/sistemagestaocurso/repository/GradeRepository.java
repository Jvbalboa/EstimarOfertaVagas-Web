package br.ufjf.coordenacao.sistemagestaocurso.repository;

import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.model.Grade;
import br.ufjf.coordenacao.sistemagestaocurso.util.jpa.EntityManagerProducer;

import java.util.List;
import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class GradeRepository implements Serializable {

	@Inject
	private EntityManager manager;
	
	private static final long serialVersionUID = 1L;

	public Grade porid(long id)

	{
		return manager.find(Grade.class, id);
	}

	public Grade persistir(Grade objeto) {
		return manager.merge(objeto);
	}

	public void remover(Grade objeto) {
		manager.createQuery("DELETE FROM Grade g WHERE g.id = :grade").setParameter("grade", objeto.getId()).executeUpdate();
	}

	public List<Grade> listarTodos() {
		return manager.createQuery("FROM Grade", Grade.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<String> buscarTodosCodigosGrade(String variavel, long idCurso) {
		return manager.createQuery("Select codigo FROM Grade WHERE codigo like :codigo and id_curso = :idcurso")
				.setParameter("codigo", "%" + variavel + "%").setParameter("idcurso", idCurso).getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<Grade> buscarTodosCodigosGradePorCurso(long idCurso) {

		return manager.createQuery("FROM Grade WHERE id_curso = :idcurso").setParameter("idcurso", idCurso)
				.getResultList();
	}

	public Grade buscarPorCodigoGrade(String grade, Curso curso) {
		try {
			return manager.createQuery("FROM Grade WHERE codigo = :codigo AND id_curso = :curso", Grade.class)
					.setParameter("codigo", grade)
					.setParameter("curso", curso.getId())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}