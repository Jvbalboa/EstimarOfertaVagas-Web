

package dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import model.EventoAce;
import dao.Interface.EventoAceDAO;

public class EventoAceDAOImpl extends HibernateGenericDAO<EventoAce, Long> implements EventoAceDAO, Serializable {

	private static final long serialVersionUID = 1L;

	public ArrayList<EventoAce> buscarPorAluno(long codigo){		
		ArrayList<EventoAce> eventoAces;
		try {
			eventoAces =  (ArrayList<EventoAce>) em.createQuery("FROM EventoAce WHERE id_aluno = :codigo order by periodo", EventoAce.class)
				.setParameter("codigo",  codigo )
				.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return eventoAces;
	}
	
	public List<EventoAce> buscarPorMatricula(String matricula)
	{
		return em.createQuery("FROM EventoAce WHERE matricula = :matricula", EventoAce.class)
				.setParameter("matricula", matricula)
				.getResultList();
	}

	@Override
	public int recuperarHorasConcluidasPorMatricula(String matricula) {
		BigDecimal result = ((BigDecimal)em.createNativeQuery("SELECT SUM( HORAS ) AS horas_totais FROM `EventoAce` WHERE MATRICULA = :matricula")
				.setParameter("matricula", matricula)
				.getSingleResult());
		
		if(result != null)
			return result.intValue();
		else
			return 0;
	}

}