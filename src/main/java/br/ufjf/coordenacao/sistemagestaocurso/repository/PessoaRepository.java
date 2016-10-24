package br.ufjf.coordenacao.sistemagestaocurso.repository;

import br.ufjf.coordenacao.sistemagestaocurso.model.Pessoa;

import java.util.List;
import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class PessoaRepository implements Serializable {

	@Inject
	private EntityManager manager;

	private static final long serialVersionUID = 1L;

	public Pessoa porid(long id) {
		return manager.find(Pessoa.class, id);
	}

	public Pessoa persistir(Pessoa objeto) {
		return manager.merge(objeto);
	}

	public void remover(Pessoa objeto) {
		manager.remove(objeto);
	}

	public List<Pessoa> listarTodos() {
		return manager.createQuery("FROM Pessoa", Pessoa.class).getResultList();
	}

	public Pessoa buscarPorNomePessoa(String variavel) {
		return manager.createQuery("FROM Pessoa WHERE nome = :codigo", Pessoa.class).setParameter("codigo", variavel)
				.getSingleResult();

	}

	public Pessoa buscarPorSiapePessoa(String variavel) {
		return manager.createQuery("FROM Pessoa WHERE siape = :codigo", Pessoa.class).setParameter("codigo", variavel)
				.getSingleResult();

	}

	public int alterarSenha(Pessoa pessoaSelecionada) {

		return manager.createQuery("UPDATE Pessoa SET SENHA = :senha WHERE id = :codigo")
				.setParameter("codigo", pessoaSelecionada.getId()).setParameter("senha", pessoaSelecionada.getSenha())
				.executeUpdate();

	}
}