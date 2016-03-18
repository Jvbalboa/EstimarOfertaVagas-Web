package dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import model.PessoaCurso;
import dao.Interface.PessoaCursoDAO;

public class PessoaCursoDAOImpl extends HibernateGenericDAO<PessoaCurso, Long> implements PessoaCursoDAO, Serializable {

	private static final long serialVersionUID = 1L;

	public List<PessoaCurso> buscarTodasPessoaCursoPorPessoa(long codigo){		
		ArrayList<PessoaCurso> pessoaCursos;
		try {
			pessoaCursos = (ArrayList<PessoaCurso>) em.createQuery("FROM PessoaCurso WHERE id_pessoa = :codigo", PessoaCurso.class)
				.setParameter("codigo",  codigo )
				.getResultList();
			
		}  catch(NoResultException e){
			return null;
		}
		return pessoaCursos;		
	}
	
	public void resetDAO(){
		
		em.clear();
	}
	
	
	
}