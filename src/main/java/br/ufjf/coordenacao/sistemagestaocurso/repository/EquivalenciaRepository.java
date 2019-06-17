package br.ufjf.coordenacao.sistemagestaocurso.repository;

import br.ufjf.coordenacao.sistemagestaocurso.model.Equivalencia;

import java.util.List;
import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class EquivalenciaRepository implements Serializable {

	@Inject
	private EntityManager manager;
	
	private static final long serialVersionUID = 1L;

	public Equivalencia porid(long id) {
		return manager.find(Equivalencia.class, id);
	}

	public Equivalencia persistir(Equivalencia objeto) {
		return manager.merge(objeto);
	}

	public void remover(Equivalencia equivalencia) {
		// Verificar se equivalencia já foi persistida no banco
		manager.remove(manager.find(Equivalencia.class, equivalencia.getId()));
	}

	public List<Equivalencia> listarTodos() {
		return manager.createQuery("FROM Equivalencia", Equivalencia.class).getResultList();
	}

	public List<Equivalencia> buscarTodasEquivalenciasPorGrade(long codigo) {
		return manager.createQuery("FROM Equivalencia WHERE id_grade = :codigo", Equivalencia.class)
				.setParameter("codigo", codigo).getResultList();
	}

	public Equivalencia buscarPorEquivalencia(long idDisciplinaEsquerda, long idDisciplinaDireita, long idGrade) {
		try {
			return manager
					.createQuery(
							"FROM Equivalencia WHERE id_disciplina = :iddisciplinaesquerda and id_disciplina_grade = :iddisciplinadireita and id_grade = :idgrade",
							Equivalencia.class)
					.setParameter("iddisciplinaesquerda", idDisciplinaEsquerda)
					.setParameter("iddisciplinadireita", idDisciplinaDireita).setParameter("idgrade", idGrade)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public boolean existeDisciplinaEquivalente(long idDisciplina, long idGrade) {
		return manager
				.createQuery(
						"FROM Equivalencia WHERE id_disciplina = :iddisciplina and id_grade = :idgrade",
						Equivalencia.class)
				.setParameter("iddisciplina", idDisciplina)
				.setParameter("idgrade", idGrade)
				.getResultList().size() > 0;
	}
}