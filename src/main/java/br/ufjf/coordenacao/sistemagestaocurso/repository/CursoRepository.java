package br.ufjf.coordenacao.sistemagestaocurso.repository;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;

public class CursoRepository {

	@Inject
	private EntityManager manager;

	public Curso porid(long id) {
		return manager.find(Curso.class, id);
	}

	public Curso persistir(Curso curso) {
		return manager.merge(curso);
	}

	public void remover(Curso curso) {
		manager.remove(curso);
	}

	public List<Curso> listarTodos() {
		return manager.createQuery("FROM Curso", Curso.class).getResultList();
	}

	public Curso buscarPorCodigo(String variavel) {
		return manager.createQuery("FROM Curso WHERE codigo = :codigo", Curso.class).setParameter("codigo", variavel)
				.getSingleResult();

	}

	public Curso buscarPorNome(String variavel) {
		return manager.createQuery("FROM Curso WHERE nome = :codigo", Curso.class).setParameter("codigo", variavel)
				.getSingleResult();

	}

	public List<String> buscarTodosCodigosCurso(String variavel) {
		return manager.createQuery("Select codigo FROM Curso WHERE codigo like :codigo", String.class)
				.setParameter("codigo", "%" + variavel + "%").getResultList();

	}

	public List<String> buscarTodosNomesCurso(String variavel) {
		return manager.createQuery("Select nome FROM Curso WHERE nome like :codigo", String.class)
				.setParameter("codigo", "%" + variavel + "%").getResultList();

	}

	public List<Curso> buscarTodosNomesObjetoCurso(String variavel) {
		return manager.createQuery("FROM Curso WHERE nome like :codigo", Curso.class)
				.setParameter("codigo", "%" + variavel + "%").getResultList();

	}
}
