package br.ufjf.coordenacao.sistemagestaocurso.repository;

import br.ufjf.coordenacao.sistemagestaocurso.model.PreRequisito;
import br.ufjf.coordenacao.sistemagestaocurso.util.jpa.EntityManagerProducer;

import java.util.List;
import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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
		manager.createQuery("DELETE FROM PreRequisito WHERE id = :id").setParameter("id", objeto.getId()).executeUpdate();

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
		try {
		return ((Long) manager .createQuery("Select id FROM PreRequisito WHERE id_grade_disciplina = :idGrade and id_disciplina = :idDisciplina")
				.setParameter("idGrade", idGrade)
				.setParameter("idDisciplina", idDisciplina)
				.getSingleResult()).intValue();
		}
		catch (NoResultException e)
		{
			return 0;
		}

	}

}