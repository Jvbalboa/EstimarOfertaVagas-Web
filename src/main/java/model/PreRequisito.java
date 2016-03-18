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
@SequenceGenerator(name="prerequisito_sequencia", sequenceName="prerequisito_seq", allocationSize=1)
public class PreRequisito {
	// ==========================VARIÃ�VEIS=================================================================================================================//

	private Disciplina disciplina;
	private GradeDisciplina gradeDisciplina;
	private Long id;
	private String tipo;

	// ==========================GETTERS_AND_SETTERS======================================================================================================//
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="prerequisito_sequencia")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="ID_GRADE_DISCIPLINA" , referencedColumnName="ID",nullable=false)
	public GradeDisciplina getGradeDisciplina() {
		return gradeDisciplina;
	}

	public void setGradeDisciplina(GradeDisciplina gradeDisciplina) {
		this.gradeDisciplina = gradeDisciplina;
	}

	@ManyToOne
	@JoinColumn(name="ID_DISCIPLINA" , referencedColumnName="ID",nullable=false)
	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	@Column (name="TIPO",  nullable = false)
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}

