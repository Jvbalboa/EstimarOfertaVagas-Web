package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name="pessoacurso_sequencia", sequenceName="pessoacurso_seq", allocationSize=1)
public class PessoaCurso {
	
	private Long id;
	private Curso curso;
	private Pessoa pessoa;	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="pessoacurso_sequencia")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	
	
	@ManyToOne
	@JoinColumn(name="ID_CURSO" , referencedColumnName="ID",nullable = false)
	public Curso getCurso() {
		return curso;
	}
	
	public void setCurso(Curso curso) {
		this.curso = curso;
	}	
	
	@ManyToOne
	@JoinColumn(name="ID_PESSOA" , referencedColumnName="ID" ,nullable = false)
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
}