package br.ufjf.coordenacao.sistemagestaocurso.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;

public class AlunoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Aluno buscarPorId(long id) {
		return manager.find(Aluno.class, id);
	}

	public Aluno persistir(Aluno aluno) {
		return manager.merge(aluno);
	}

	public void remover(Aluno aluno) {
		manager.remove(manager.contains(aluno) ? aluno : manager.merge(aluno));
	}

	public Aluno buscarPorMatricula(String variavel) {
		try {
			return manager.createQuery("FROM Aluno WHERE matricula = :codigo", Aluno.class)
					.setParameter("codigo", variavel).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Aluno buscarPorNome(String variavel) {
		try {
			return manager.createQuery("FROM Aluno WHERE nome = :codigo", Aluno.class).setParameter("codigo", variavel)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> buscarTodosAlunoMatricula(String variavel, Long idcurso) {
		try {
			return manager
					.createQuery("Select matricula FROM Aluno WHERE matricula like :codigo and id_curso = :idcurso")
					.setParameter("codigo", "%" + variavel + "%").setParameter("idcurso", idcurso).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> buscarTodosAlunoNome(String variavel, Long idcurso) {
		try {
			return manager.createQuery("Select nome FROM Aluno WHERE nome like :codigo and id_curso = :idcurso")
					.setParameter("codigo", "%" + variavel + "%").setParameter("idcurso", idcurso).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> buscarTodosAlunoNomeObjeto(String variavel, Long idcurso) {
		try {
			return manager.createQuery("FROM Aluno WHERE nome like :codigo and id_curso = :idcurso")
					.setParameter("codigo", "%" + variavel + "%").setParameter("idcurso", idcurso).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> buscarTodosAlunoCursoGradeObjeto(Long idCurso, Long idGrade) {
		return manager.createQuery("FROM Aluno WHERE id_curso = :idCurso and id_grade = :idGrade ")
				.setParameter("idCurso", idCurso).setParameter("idGrade", idGrade).getResultList();

	}

	public List<Aluno> buscarTodosAlunoCursoObjeto(Long idCurso) {
		return manager.createQuery("FROM Aluno WHERE id_curso = :idCurso  ", Aluno.class)
				.setParameter("idCurso", idCurso).getResultList();

	}
}
