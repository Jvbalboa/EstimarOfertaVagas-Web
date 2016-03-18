package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;


@Entity
@SequenceGenerator(name="pessoa_sequencia", sequenceName="pessoa_seq", allocationSize=1) 
public class Pessoa {	
	
	private Long id;
	private String siape;
	private String nome;
	private String senha;
	private List<PessoaCurso> pessoaCurso = new ArrayList<PessoaCurso>();	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="pessoa_sequencia")  
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}	
	
	@Column (name="SIAPE", unique = true, nullable = false)
	public String getSiape() {
		return siape;
	}

	public void setSiape(String siape) {
		this.siape = siape;
	}

	
	@Column (name="NOME")
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}	
	
	@OneToMany(mappedBy = "pessoa", targetEntity = PessoaCurso.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<PessoaCurso> getPessoaCurso() {
		return pessoaCurso;
	}
	
	public void setPessoaCurso(List<PessoaCurso> pessoaCurso) {
		this.pessoaCurso = pessoaCurso;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}