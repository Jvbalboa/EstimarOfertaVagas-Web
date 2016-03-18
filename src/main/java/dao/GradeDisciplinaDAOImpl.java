package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import model.GradeDisciplina;
import dao.Interface.GradeDisciplinaDAO;

public class GradeDisciplinaDAOImpl extends HibernateGenericDAO<GradeDisciplina, Long> implements GradeDisciplinaDAO, Serializable {

	private static final long serialVersionUID = 1L;

	public ArrayList<GradeDisciplina> buscarTodasGradeDisciplinaPorGrade(long codigo){
		ArrayList<GradeDisciplina> listaCodigos;
		try {
			listaCodigos = (ArrayList<GradeDisciplina>) em.createQuery("FROM GradeDisciplina WHERE id_grade = :codigo order by periodo", GradeDisciplina.class)
					.setParameter("codigo",  codigo )
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return listaCodigos;
	}

	public ArrayList<GradeDisciplina> buscarTodasGradeDisciplinaPorGradeId(long idGrade){
		ArrayList<GradeDisciplina> listaCodigos;
		try {
			listaCodigos = (ArrayList<GradeDisciplina>) em.createQuery("FROM GradeDisciplina WHERE id_grade = :idGrade order by periodo", GradeDisciplina.class)
					.setParameter("idGrade",  idGrade )
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return listaCodigos;
	}

	public GradeDisciplina buscarPorPK(String tipo,Long periodo,Long idGrade,Long idDisciplina) {
		GradeDisciplina grades;
		try {
			Query query = em.createQuery("FROM GradeDisciplina WHERE tipo = :tipo and periodo = :periodo");
			query.setParameter("tipo", tipo);
			query.setParameter("periodo", periodo);			    
			grades =  (GradeDisciplina) query.getSingleResult();
		}  catch(NoResultException e){
			return null;
		}
		return grades;
	}

	public GradeDisciplina buscarPorDisciplinaGrade(Long idGrade,Long idDisciplina) {
		GradeDisciplina grades;
		try {
			Query query = em.createQuery("FROM GradeDisciplina WHERE id_disciplina = :idDisciplina and id_grade = :idGrade");
			query.setParameter("idGrade", idGrade);
			query.setParameter("idDisciplina", idDisciplina);		    
			grades =  (GradeDisciplina) query.getSingleResult();
		}  catch(NoResultException e){
			return null;
		}
		return grades;
	}
	
	public GradeDisciplina buscarPorDisciplinaGradeIra(Long idGrade,Long idDisciplina) {
		GradeDisciplina grades;
		try {
			Query query = em.createQuery("FROM GradeDisciplina WHERE id_disciplina = :idDisciplina and id_grade = :idGrade and excluir_ira = true");
			query.setParameter("idGrade", idGrade);
			query.setParameter("idDisciplina", idDisciplina);		    
			grades =  (GradeDisciplina) query.getSingleResult();
		}  catch(NoResultException e){
			return null;
		}
		return grades;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<GradeDisciplina> buscarPorTipoGrade(Long idGrade,String tipo){
		ArrayList<GradeDisciplina> grades;
		try {
			Query query = em.createQuery("FROM GradeDisciplina WHERE id_grade = :idGrade and tipo_disciplina = :tipo");
			query.setParameter("idGrade", idGrade);
			query.setParameter("tipo", tipo);		    
			grades =  (ArrayList<GradeDisciplina>) query.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return grades;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<GradeDisciplina> buscarPorIra(Long idGrade,boolean ira){
		ArrayList<GradeDisciplina> grades;
		try {
			Query query = em.createQuery("FROM GradeDisciplina WHERE id_grade = :idGrade and excluir_ira = :ira");
			query.setParameter("idGrade", idGrade);
			query.setParameter("ira", ira);		    
			grades =  (ArrayList<GradeDisciplina>) query.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return grades;
	}
}