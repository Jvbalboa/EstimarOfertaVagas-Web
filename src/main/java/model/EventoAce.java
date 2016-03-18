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
@SequenceGenerator(name="eventoace_sequencia", sequenceName="eventoace_seq", allocationSize=1)  
public class EventoAce {
	
	// ==========================VARIÃ�VEIS=================================================================================================================//

	private Long id;
	private Long horas;
	private Aluno aluno;
	private String descricao;
	private Integer periodo;
	private Boolean excluir;

	// ==========================GETTERS_AND_SETTERS======================================================================================================//

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="eventoace_sequencia")  
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}	
	
	@Column(name="PERIODO" ,nullable = false)
	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	@Column(name="DESCRICAO" ,nullable = false)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name="HORAS" ,nullable = false)
	public Long getHoras() {
		return horas;
	}

	public void setHoras(Long horas) {
		this.horas = horas;
	}

	@ManyToOne
	@JoinColumn(name="ID_ALUNO" , referencedColumnName="ID")
	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Boolean getExcluir() {
		return excluir;
	}

	public void setExcluir(Boolean excluir) {
		this.excluir = excluir;
	}	
}