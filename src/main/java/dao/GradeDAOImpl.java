package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import model.Grade;
import dao.Interface.GradeDAO;

public class GradeDAOImpl extends HibernateGenericDAO<Grade, Long> implements GradeDAO, Serializable {

	private static final long serialVersionUID = 1L;


	@SuppressWarnings("unchecked")
	public ArrayList<String> buscarTodosCodigosGrade(String variavel,long idCurso){
		ArrayList<String> grades;
		try {
			
			Query query = em.createQuery("Select codigo FROM Grade WHERE codigo like :codigo and id_curso = :idcurso");
			query.setParameter("codigo", "%" + variavel + "%");
			query.setParameter("idcurso",  idCurso );			    
			grades = (ArrayList<String>) query.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return grades;
	}	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Grade> buscarTodosCodigosGradePorCurso(long idCurso){
		ArrayList<Grade> grades;
		try {
			Query query = em.createQuery("FROM Grade WHERE id_curso = :idcurso");
			query.setParameter("idcurso",  idCurso );			    
			grades = (ArrayList<Grade>) query.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return grades;
	}

	public Grade buscarPorCodigoGrade(String variavel) {
		Grade grades;
		try {
			Query query = em.createQuery("FROM model.Grade WHERE id = :codigo");
			query.setParameter("codigo", variavel);	    
			grades =  (Grade) query.getSingleResult();
		}  catch(NoResultException e){
			return null;
		}
		return grades;
	}
}