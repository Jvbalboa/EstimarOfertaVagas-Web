package br.ufjf.coordenacao.sistemagestaocurso.repository;

import br.ufjf.coordenacao.sistemagestaocurso.model.PreRequisito;

import java.util.List;
import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class PreRequisitoRepository implements Serializable {

	@Inject
	private EntityManager manager;

	private static final long serialVersionUID = 1L;

	public PreRequisito porid(long id) {
		return manager.find(PreRequisito.class, id);
	}

	public PreRequisito persistir(PreRequisito objeto) {
		return manager.merge(objeto);
	}

	public void remover(PreRequisito objeto) {
		manager.remove(manager.contains(objeto) ? objeto : manager.merge(objeto));
	}

	public List<PreRequisito> listarTodos() {
		return manager.createQuery("FROM PreRequisito", PreRequisito.class).getResultList();
	}

	public List<PreRequisito> buscarPorTodosCodigoGradeDisc(Long codigo) {
		return manager.createQuery("FROM PreRequisito WHERE id_grade_disciplina = :codigo", PreRequisito.class)
				.setParameter("codigo", codigo).getResultList();
	}

	public List<PreRequisito> buscarPorTodosCodigoDisciplina(Long codigo) {
		return manager.createQuery("FROM PreRequisito WHERE id_disciplina = :codigo", PreRequisito.class)
				.setParameter("codigo", codigo).getResultList();
	}

	public Integer buscarPorDisciplanaGradeId(Long idGrade, Long idDisciplina) {

		return (int) manager .createQuery("Select id FROM PreRequisito WHERE id_grade_disciplina = :idGrade and id_disciplina = :idDisciplina")
				.setParameter("idGrade", idGrade)
				.setParameter("idDisciplina", idDisciplina)
				.getSingleResult();

	}

}