package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.NoResultException;

import model.Historico;
import dao.Interface.HistoricoDAO;

public class HistoricoDAOImpl extends HibernateGenericDAO<Historico, Long> implements HistoricoDAO, Serializable {

	private static final long serialVersionUID = 1L;

	public ArrayList<Historico> buscarTodosHistoricosPorMatricula(long codigo){
		ArrayList<Historico> historicos;
		try {
			historicos = (ArrayList<Historico>) em.createQuery("FROM Historico WHERE id_matricula = :codigo", Historico.class)
					.setParameter("codigo",  codigo )
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return historicos;
	}

	public ArrayList<Historico> buscarTodosHistoricosPorSemestre(String codigo){
		ArrayList<Historico> historicos;
		try {
			historicos = (ArrayList<Historico>) em.createQuery("FROM Historico WHERE semestre_cursado = :codigo order by id_matricula", Historico.class)
					.setParameter("codigo",  codigo )
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return historicos;
	}
}