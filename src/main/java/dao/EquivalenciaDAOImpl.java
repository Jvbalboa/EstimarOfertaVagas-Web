package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import model.Equivalencia;
import dao.Interface.EquivalenciaDAO;

public class EquivalenciaDAOImpl extends HibernateGenericDAO<Equivalencia, Long> implements EquivalenciaDAO, Serializable {

	private static final long serialVersionUID = 1L;

	public ArrayList<Equivalencia> buscarTodasEquivalenciasPorGrade(long codigo){
		ArrayList<Equivalencia> equivalencias;
		try {
			equivalencias =  (ArrayList<Equivalencia>) em.createQuery("FROM Equivalencia WHERE id_grade = :codigo", Equivalencia.class)
					.setParameter("codigo",  codigo )
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return equivalencias;
	}

	public Equivalencia buscarPorEquivalencia(long idDisciplinaEsquerda,long idDisciplinaDireita,long idGrade) {
		Equivalencia equivalencia;
		try {
			Query query = em.createQuery("FROM Equivalencia WHERE id_disciplina = :iddisciplinaesquerda and id_disciplina_grade = :iddisciplinadireita and id_grade = :idgrade");
			query.setParameter("iddisciplinaesquerda", idDisciplinaEsquerda);
			query.setParameter("iddisciplinadireita", idDisciplinaDireita);
			query.setParameter("idgrade", idGrade);
			equivalencia =  (Equivalencia) query.getSingleResult();
		}  catch(NoResultException e){
			return null;
		}
		return equivalencia;
	}
}