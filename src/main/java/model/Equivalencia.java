package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;



@Entity
@SequenceGenerator(name="equivalencia_sequencia", sequenceName="equivalencia_seq", allocationSize=1)
public class Equivalencia {
	// ==========================VARIÃ�VEIS=================================================================================================================//

	private Grade grade;
	private Disciplina disciplinaEquivalente;
	private Disciplina disciplinaGrade;
	private Long id;

	// ==========================GETTERS_AND_SETTERS======================================================================================================//

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="equivalencia_sequencia")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="ID_DISCIPLINA" , referencedColumnName="ID",nullable = false)		
	public Disciplina getDisciplinaEquivalente() {
		return disciplinaEquivalente;
	}

	public void setDisciplinaEquivalente(Disciplina disciplinaEquivalente) {
		this.disciplinaEquivalente = disciplinaEquivalente;
	}

	@ManyToOne
	@JoinColumn(name="ID_DISCIPLINA_GRADE" , referencedColumnName="ID",nullable = false)		
	public Disciplina getDisciplinaGrade() {
		return disciplinaGrade;
	}

	public void setDisciplinaGrade(Disciplina disciplinaGrade) {
		this.disciplinaGrade = disciplinaGrade;
	}

	@ManyToOne
	@JoinColumn(name="ID_GRADE" , referencedColumnName="ID",nullable = false)
	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}
}