package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.NoResultException;

import model.Curso;
import dao.Interface.CursoDAO;

public class CursoDAOImpl extends HibernateGenericDAO<Curso, Long> implements CursoDAO, Serializable {

	private static final long serialVersionUID = 1L;

	public Curso buscarPorCodigo(String variavel) {
		Curso curso;
		try {
			curso =  em.createQuery("FROM Curso WHERE codigo = :codigo", Curso.class)
					.setParameter("codigo", variavel)
					.getSingleResult();

		}  catch(NoResultException e){
			return null;
		}
		return curso;
	}

	public Curso buscarPorNome(String variavel) {
		Curso curso;
		try {	
			curso =  em.createQuery("FROM Curso WHERE nome = :codigo", Curso.class)
					.setParameter("codigo", variavel)
					.getSingleResult();
		}  catch(NoResultException e){
			return null;
		}
		return curso;
	}

	public ArrayList<String> buscarTodosCodigosCurso(String variavel){
		ArrayList<String> listaCodigos;
		try {
			listaCodigos =  (ArrayList<String>) em.createQuery("Select codigo FROM Curso WHERE codigo like :codigo", String.class)
					.setParameter("codigo", "%" + variavel + "%")
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return listaCodigos;
	}

	public ArrayList<String> buscarTodosNomesCurso(String variavel){
		ArrayList<String> listaCodigos;
		try {
			listaCodigos = (ArrayList<String>) em.createQuery("Select nome FROM Curso WHERE nome like :codigo", String.class)
					.setParameter("codigo", "%" + variavel + "%")
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return listaCodigos;
	}

	public ArrayList<Curso> buscarTodosNomesObjetoCurso(String variavel){
		ArrayList<Curso> listaCodigos;
		try {
			listaCodigos =  (ArrayList<Curso>) em.createQuery("FROM Curso WHERE nome like :codigo", Curso.class)
					.setParameter("codigo", "%" + variavel + "%")
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return listaCodigos;
	}
}