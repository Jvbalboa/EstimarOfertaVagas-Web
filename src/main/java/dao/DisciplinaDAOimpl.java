package dao;


import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.NoResultException;

import model.Disciplina;
import dao.Interface.DisciplinaDAO;

public class DisciplinaDAOimpl extends HibernateGenericDAO<Disciplina, Long> implements DisciplinaDAO, Serializable {

	private static final long serialVersionUID = 1L;

	public ArrayList<String> buscarTodosCodigosDisciplina(String variavel){
		ArrayList<String> listaCodigos;
		try {
			listaCodigos = (ArrayList<String>) em.createQuery("Select codigo FROM Disciplina WHERE codigo like :codigo order by codigo", String.class)
					.setParameter("codigo", "%" + variavel + "%")
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return listaCodigos;
	}

	public Disciplina buscarPorCodigoDisciplina(String variavel) {
		Disciplina disciplina;
		try {	
			disciplina =  em.createQuery("FROM Disciplina WHERE codigo = :codigo", Disciplina.class)
					.setParameter("codigo", variavel)
					.getSingleResult();
		}  catch(NoResultException e){
			return null;
		}
		return disciplina;
	}

	public ArrayList<String> buscarTodosNomesDisciplina(String variavel){
		ArrayList<String> listaCodigos;
		try {
			listaCodigos = (ArrayList<String>) em.createQuery("Select nome FROM Disciplina WHERE nome like :codigo", String.class)
					.setParameter("codigo", "%" + variavel + "%")
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return listaCodigos;
	}

	public ArrayList<Disciplina> buscarTodosNomesDisciplinaObjeto(String variavel){
		ArrayList<Disciplina> listaCodigos;
		try {
			listaCodigos =  (ArrayList<Disciplina>) em.createQuery("FROM Disciplina WHERE nome like :codigo", Disciplina.class)
					.setParameter("codigo", "%" + variavel + "%")
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return listaCodigos;
	}

	public Disciplina buscarPorNomeDisciplina(String variavel) {
		Disciplina disciplina;
		try {	
			disciplina = em.createQuery("FROM Disciplina WHERE nome = :codigo", Disciplina.class)
					.setParameter("codigo", variavel)
					.getSingleResult();
		}  catch(NoResultException e){
			return null;
		}
		return disciplina;
	}
}
