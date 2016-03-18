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
@SequenceGenerator(name="disciplina_sequencia", sequenceName="disciplina_seq", allocationSize=1)
public class Disciplina {
	// ==========================VARIÃ�VEIS=================================================================================================================//

	private String codigo;
	private String nome;
	private Integer cargaHoraria;
	private List<Historico> grupoHistorico = new ArrayList<Historico>();
	private List<GradeDisciplina> grupoGradeDisciplina = new ArrayList<GradeDisciplina>();
	private List<Equivalencia> grupoEquivalencia = new ArrayList<Equivalencia>();
	private List<Equivalencia> grupoEquivalenciaGrade = new ArrayList<Equivalencia>();
	private Long id;
	
	// ==========================GETTERS_AND_SETTERS======================================================================================================//

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="disciplina_sequencia")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	

	@Column (name="CODIGO" , unique = true, nullable = false)
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(name="NOME" )
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Column(name="CARGA_HORARIA")
	public Integer getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	@OneToMany(mappedBy = "disciplina", targetEntity = Historico.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Historico> getGrupoHistorico() {
		return grupoHistorico;
	}

	public void setGrupoHistorico(List<Historico> grupoHistorico) {
		this.grupoHistorico = grupoHistorico;
	}

	@OneToMany(mappedBy = "disciplina", targetEntity = GradeDisciplina.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<GradeDisciplina> getGrupoGradeDisciplina() {
		return grupoGradeDisciplina;
	}

	public void setGrupoGradeDisciplina(List<GradeDisciplina> grupoGradeDisciplina) {
		this.grupoGradeDisciplina = grupoGradeDisciplina;
	}

	@OneToMany(mappedBy = "disciplinaEquivalente", targetEntity = Equivalencia.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Equivalencia> getGrupoEquivalencia() {
		return grupoEquivalencia;
	}

	public void setGrupoEquivalencia(List<Equivalencia> grupoEquivalencia) {
		this.grupoEquivalencia = grupoEquivalencia;
	}

	@OneToMany(mappedBy = "disciplinaGrade", targetEntity = Equivalencia.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Equivalencia> getGrupoEquivalenciaGrade() {
		return grupoEquivalenciaGrade;
	}

	public void setGrupoEquivalenciaGrade(List<Equivalencia> grupoEquivalenciaGrade) {
		this.grupoEquivalenciaGrade = grupoEquivalenciaGrade;
	}

}