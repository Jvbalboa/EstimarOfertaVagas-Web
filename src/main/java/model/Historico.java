package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;



@Entity
@SequenceGenerator(name="historico_sequencia", sequenceName="historico_seq", allocationSize=1)  
public class Historico {
	// ==========================VARIÃ�VEIS=================================================================================================================//

	private Disciplina disciplina;
	private String semestreCursado;
	private Aluno aluno;
	private String statusDisciplina ;
	private Long id;
	private String nota;

	// ==========================GETTERS_AND_SETTERS======================================================================================================//

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="historico_sequencia")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="SEMESTRE_CURSADO")
	public String getSemestreCursado() {
		return semestreCursado;
	}

	public void setSemestreCursado(String semestreCursado) {
		this.semestreCursado = semestreCursado;
	}

	@Column(name="STATUS_DISCIPLINA")
	public String getStatusDisciplina() {
		return statusDisciplina;
	}

	public void setStatusDisciplina(String statusDisciplina) {
		this.statusDisciplina = statusDisciplina;
	}

	@ManyToOne
	@JoinColumn(name="ID_DISCIPLINA" , referencedColumnName="ID",nullable = false)
	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	@ManyToOne
	@JoinColumn(name="ID_MATRICULA" , referencedColumnName="ID",nullable = false)
	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	
	@Column(name="NOTA")
	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}
}