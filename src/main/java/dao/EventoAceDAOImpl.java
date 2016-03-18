

package dao;

import java.io.Serializable;
import java.util.ArrayList;

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
}