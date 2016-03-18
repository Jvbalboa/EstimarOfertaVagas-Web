package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import model.Aluno;
import dao.Interface.AlunoDAO;

public class AlunoDAOImpl extends HibernateGenericDAO<Aluno, Long> implements AlunoDAO, Serializable {

	private static final long serialVersionUID = 1L;

	public Aluno buscarPorMatricula(String variavel) {
		Aluno aluno;
		try {
			aluno =  em.createQuery("FROM Aluno WHERE matricula = :codigo", Aluno.class)
					.setParameter("codigo", variavel)
					.getSingleResult();
		}  catch(NoResultException e){
			return null;
		}
		return aluno;
	}

	public Aluno buscarPorNome(String variavel) {
		Aluno aluno;
		try {
			aluno = em.createQuery("FROM Aluno WHERE nome = :codigo", Aluno.class)
					.setParameter("codigo", variavel)
					.getSingleResult();
		}  catch(NoResultException e){
			return null;
		}
		return aluno;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> buscarTodosAlunoMatricula(String variavel,Long idcurso){
		ArrayList<String> alunoMatricula;
		try {
			Query query = em.createQuery("Select matricula FROM Aluno WHERE matricula like :codigo and id_curso = :idcurso");
			query.setParameter("codigo", "%" + variavel + "%");
			query.setParameter("idcurso", idcurso);      
			alunoMatricula = (ArrayList<String>) query.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return alunoMatricula;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> buscarTodosAlunoNome(String variavel,Long idcurso){
		ArrayList<String> alunoNome;
		try {
			Query query = em.createQuery("Select nome FROM Aluno WHERE nome like :codigo and id_curso = :idcurso");
			query.setParameter("codigo", "%" + variavel + "%");
			query.setParameter("idcurso", idcurso);      
			alunoNome = (ArrayList<String>) query.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return alunoNome;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Aluno> buscarTodosAlunoNomeObjeto(String variavel,Long idcurso){
		ArrayList<Aluno> alunoNome;
		try {
			Query query = em.createQuery("FROM Aluno WHERE nome like :codigo and id_curso = :idcurso");
			query.setParameter("codigo", "%" + variavel + "%");
			query.setParameter("idcurso", idcurso);
			alunoNome = (ArrayList<Aluno>) query.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return alunoNome;
	}


	@SuppressWarnings("unchecked")
	public ArrayList<Aluno> buscarTodosAlunoCursoGradeObjeto(Long idCurso,Long idGrade ){
		ArrayList<Aluno> alunoNome;
		try {
			Query query = em.createQuery("FROM Aluno WHERE id_curso = :idCurso and id_grade = :idGrade ");
			query.setParameter("idCurso",  idCurso );
			query.setParameter("idGrade",  idGrade );
			alunoNome = (ArrayList<Aluno>) query.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return alunoNome;
	}

	public ArrayList<Aluno> buscarTodosAlunoCursoObjeto(Long idCurso){
		ArrayList<Aluno> alunos;
		try {
			alunos = (ArrayList<Aluno>) em.createQuery("FROM Aluno WHERE id_curso = :idCurso  ", Aluno.class)
					.setParameter("idCurso",  idCurso )
					.getResultList();
		}  catch(NoResultException e){
			return null;
		}
		return alunos;
	}
}