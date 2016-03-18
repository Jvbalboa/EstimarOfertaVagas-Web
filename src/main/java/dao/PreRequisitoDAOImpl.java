package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import model.PreRequisito;
import dao.Interface.PreRequisitoDAO;

public class PreRequisitoDAOImpl extends HibernateGenericDAO<PreRequisito, Long> implements PreRequisitoDAO, Serializable {

	private static final long serialVersionUID = 1L;

	public ArrayList<PreRequisito> buscarPorTodosCodigoGradeDisc(Long codigo){		
		ArrayList<PreRequisito> preRequisitos;
		try {
			preRequisitos = (ArrayList<PreRequisito>) em.createQuery("FROM PreRequisito WHERE id_grade_disciplina = :codigo", PreRequisito.class)
				.setParameter("codigo",  codigo )
				.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return preRequisitos;
	}

	public ArrayList<PreRequisito> buscarPorTodosCodigoDisciplina(Long codigo){
		ArrayList<PreRequisito> preRequisitos;
		try {
			preRequisitos = (ArrayList<PreRequisito>) em.createQuery("FROM PreRequisito WHERE id_disciplina = :codigo", PreRequisito.class)
				.setParameter("codigo",  codigo )
				.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return preRequisitos;
	}

	public Integer buscarPorDisciplanaGradeId(Long idGrade ,Long idDisciplina) {
		try {
			Query query = em.createQuery("Select id FROM PreRequisito WHERE id_grade_disciplina = :idGrade and id_disciplina = :idDisciplina");
			query.setParameter("idGrade", idGrade);
			query.setParameter("idDisciplina", idDisciplina);     
			query.getSingleResult();			
		}  catch(NoResultException e){
			return 0;
		}
		return 1;
	}
}