package br.ufjf.coordenacao.sistemagestaocurso.repository;

import br.ufjf.coordenacao.sistemagestaocurso.model.Grade;

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
		manager.remove(objeto);
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

	public Grade buscarPorCodigoGrade(String variavel) {
		try {
			return manager.createQuery("FROM Grade WHERE codigo = :codigo", Grade.class)
					.setParameter("codigo", variavel).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}