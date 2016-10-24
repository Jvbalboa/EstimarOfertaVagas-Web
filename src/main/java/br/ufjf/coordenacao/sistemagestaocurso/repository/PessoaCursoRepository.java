package br.ufjf.coordenacao.sistemagestaocurso.repository;

import br.ufjf.coordenacao.sistemagestaocurso.model.PessoaCurso;

import java.util.List;
import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class PessoaCursoRepository implements Serializable {

	@Inject
	private EntityManager manager;

	private static final long serialVersionUID = 1L;

	public PessoaCurso porid(long id) {
		return manager.find(PessoaCurso.class, id);
	}

	public PessoaCurso persistir(PessoaCurso objeto) {
		return manager.merge(objeto);
	}

	public void remover(PessoaCurso objeto) {
		manager.remove(objeto);
	}

	public List<PessoaCurso> listarTodos() {
		return manager.createQuery("FROM PessoaCurso", PessoaCurso.class).getResultList();
	}

	public List<PessoaCurso> buscarTodasPessoaCursoPorPessoa(long codigo){		
		return manager.createQuery("FROM PessoaCurso WHERE id_pessoa = :codigo", PessoaCurso.class)
				.setParameter("codigo",  codigo )
				.getResultList();
			
	}
}