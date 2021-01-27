package br.ufjf.coordenacao.sistemagestaocurso.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;

public class AlunoRepository implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(AlunoRepository.class);

	@Inject
	private EntityManager manager;
	

	public Aluno buscarPorId(long id) {
		return manager.find(Aluno.class, id);
	}

	public Aluno persistir(Aluno aluno) {
		EntityTransaction transaction = null;
		
		try {
			transaction = manager.getTransaction();
			if (!transaction.isActive())
				transaction.begin();
			aluno = manager.merge(aluno);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
		
		return aluno;
	}
	
	public List<Aluno> persistir(List<Aluno> alunos) {
		EntityTransaction transaction = null;
		
		List<Aluno> aux = new ArrayList<Aluno>();
		
		try {
			transaction = manager.getTransaction();
			if (!transaction.isActive())
				transaction.begin();
			//alunos = manager.merge(alunos);
			for(Aluno aluno : alunos) {
				aluno = manager.merge(aluno);
				aux.add(aluno);
				//manager.flush();
				//manager.clear();
			}
			transaction.commit();
			return aux;
		} catch (Exception e) {
			//transaction.rollback();
			throw e;
		}
	}
	
	public List<Aluno> persistirSemCommit(List<Aluno> alunos) {
		EntityTransaction transaction = null;
		
		List<Aluno> aux = new ArrayList<Aluno>();
		
		try {
			transaction = manager.getTransaction();
			if (!transaction.isActive())
				transaction.begin();
			//alunos = manager.merge(alunos);
			for(Aluno aluno : alunos) {
				aluno = manager.merge(aluno);
				//manager.flush();
				aux.add(aluno);
				//manager.clear();
			}
			//transaction.commit();
			return aux;
		} catch (Exception e) {
			//transaction.rollback();
			throw e;
		}
	}

	public void remover(Aluno aluno) {
		EntityTransaction transaction = null;
		
		try {
			transaction = manager.getTransaction();
			transaction.begin();
			manager.remove(manager.contains(aluno) ? aluno : manager.merge(aluno));
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	
	public void remover(List<Aluno> alunos) {
		EntityTransaction transaction = null;
		
		try {
			transaction = manager.getTransaction();
			if (!transaction.isActive())
				transaction.begin();
			for(Aluno aluno : alunos) {
				manager.remove(manager.contains(aluno) ? aluno : manager.merge(aluno));
			}
			transaction.commit();
		} catch (Exception e) {
			//transaction.rollback();
			throw e;
		}
	}

	public Aluno buscarPorMatricula(String variavel) {
		try {
			return manager.createQuery("FROM Aluno WHERE matricula = :codigo", Aluno.class)
					.setParameter("codigo", variavel).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Aluno buscarPorNome(String variavel) {
		try {
			return manager.createQuery("FROM Aluno WHERE nome = :codigo", Aluno.class).setParameter("codigo", variavel)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> buscarTodosAlunoMatricula(String variavel, Long idcurso) {
		try {
			return manager
					.createQuery("Select matricula FROM Aluno WHERE matricula like :codigo and id_curso = :idcurso")
					.setParameter("codigo", "%" + variavel + "%").setParameter("idcurso", idcurso).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> buscarTodosAlunoNome(String variavel, Long idcurso) {
		try {
			return manager.createQuery("Select nome FROM Aluno WHERE nome like :codigo and id_curso = :idcurso")
					.setParameter("codigo", "%" + variavel + "%").setParameter("idcurso", idcurso).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> buscarTodosAlunoNomeObjeto(String variavel, Long idcurso) {
		try {
			return manager.createQuery("FROM Aluno WHERE nome like :codigo and id_curso = :idcurso")
					.setParameter("codigo", "%" + variavel + "%").setParameter("idcurso", idcurso).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> buscarTodosAlunoCursoGradeObjeto(Long idCurso, Long idGrade) {
		return manager.createQuery("FROM Aluno WHERE id_curso = :idCurso and id_grade = :idGrade ")
				.setParameter("idCurso", idCurso).setParameter("idGrade", idGrade).getResultList();

	}

	public List<Aluno> buscarTodosAlunoCursoObjeto(Long idCurso) {
		logger.info("Buscando todos os alunos");
		try {
			return manager.createQuery("FROM Aluno WHERE id_curso = :idCurso  ", Aluno.class)
					.setParameter("idCurso", idCurso).getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
	
}
