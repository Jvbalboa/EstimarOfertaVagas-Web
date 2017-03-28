package br.ufjf.coordenacao.sistemagestaocurso.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.*;
import br.ufjf.coordenacao.OfertaVagas.model.*;
import br.ufjf.coordenacao.OfertaVagas.model.Class;

@Entity
@SequenceGenerator(name="aluno_sequencia", sequenceName="aluno_seq", allocationSize=1)  
public class Aluno {
	// ==========================VARI√ÉÔøΩVEIS=================================================================================================================//

	private Long id;
	private String matricula;
	private Curso curso;
	private Grade grade;
	private String nome;
	private Float ira;
	private Integer periodoReal;
	private List<Historico> grupoHistorico;
	private List<EventoAce> listaEventosAce;
	private int horasObrigatoriasCompletadas;
	private int horasEletivasCompletadas;
	private int sobraHorasEletivas;
	private int horasOpcionaisCompletadas;
	private int horasAceConcluidas;
	private int sobraHorasOpcionais;

	private boolean horasCalculadas; 

	// ==========================GETTERS_AND_SETTERS======================================================================================================//

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="aluno_sequencia")  
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@Column (name="MATRICULA", unique = true, nullable = false)
	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}


	@Column(name="NOME")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}	

	@Transient
	public String getPeriodoIngresso() {

		String periodoInicioLocal = "";
		if (matricula != null){
			periodoInicioLocal =  matricula.substring(0,4) ;
		}		
		return periodoInicioLocal;
	}

	@ManyToOne
	@JoinColumn(name="ID_CURSO" , referencedColumnName="ID")
	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	@ManyToOne
	@JoinColumn(name="ID_GRADE" , referencedColumnName="ID",nullable = false)
	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	@OneToMany(mappedBy = "aluno", targetEntity = Historico.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Historico> getGrupoHistorico() {
		return grupoHistorico;
	}

	public void setGrupoHistorico(List<Historico> grupoHistorico) {
		this.grupoHistorico = grupoHistorico;
	}

	@OneToMany(mappedBy = "aluno", targetEntity = EventoAce.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<EventoAce> getListaEventosAce() {
		return listaEventosAce;
	}

	public void setListaEventosAce(List<EventoAce> listaEventosAce) {
		this.listaEventosAce = listaEventosAce;
	}

	@Transient
	public Integer getPeriodoCorrente(String periodoAtual) {

		String periodoInicioLocal;
		if (grade.getPeriodoInicio() == 0){
			periodoInicioLocal = matricula.substring(0,4) + "1";
		}
		else {
			periodoInicioLocal =  matricula.substring(0,4)  + String.valueOf(grade.getPeriodoInicio());
		}
		return periodoCorrente( periodoInicioLocal,periodoAtual);
	}

	@Transient
	public Integer getPeriodoReal() {
		return periodoReal;
	}

	public void setPeriodoReal(Integer periodoReal) {
		this.periodoReal = periodoReal;
	}

	@Transient
	public Float getIra() {
		return ira;
	}


	public void setIra(Float ira) {
		this.ira = ira;
	}

	public int periodoCorrente(String ingresso,String semestreAtual){

		/*Calendar now = Calendar.getInstance();
		int anoAtual = now.get(Calendar.YEAR);
		int mes = now.get(Calendar.MONTH) + 1;
		
		int periodoAtual = 0;
		if(mes >= 1 && mes <= 6){
			periodoAtual = 1;
		}
		else {
			periodoAtual = 3;
		}*/
		
		int i = 1;
		
		int anoAtual = Integer.parseInt(semestreAtual.substring(0, 4));
		int periodoAtual = Integer.parseInt(semestreAtual.substring(4, 5));
		
		int anoIngresso = Integer.parseInt(ingresso.substring(0, 4));
		int periodoIngresso = Integer.parseInt(ingresso.substring(4, 5));
		while( anoIngresso != anoAtual || periodoAtual != periodoIngresso  ){
			i++;
			if (periodoIngresso == 3){
				anoIngresso++;
				periodoIngresso = 1;
			}
			else{
				periodoIngresso = 3;
			}
		}
		return i;
	}

	@Transient
	public String getTurma() {

		return matricula.substring(0,4);
	}
	
	@Transient
	public void calculaHorasCompletadas()
	{
		ContadorHorasIntegralizadas contador = new ContadorHorasIntegralizadas(this);
		this.horasAceConcluidas = contador.getHorasAceConcluidas();
		this.horasEletivasCompletadas = contador.getHorasEletivasCompletadas();
		this.horasObrigatoriasCompletadas = contador.getHorasObrigatoriasCompletadas();
		this.horasOpcionaisCompletadas = contador.getHorasOpcionaisCompletadas();
		this.sobraHorasEletivas = contador.getSobraHorasEletivas();
		this.sobraHorasOpcionais = contador.getSobraHorasOpcionais();
		this.horasCalculadas = true;
	}
	
	@Transient
	public int getHorasObrigatoriasCompletadas() {
		if(!this.horasCalculadas)
			this.calculaHorasCompletadas();
			
		return this.horasObrigatoriasCompletadas;
	}

	@Transient
	public int getHorasEletivasCompletadas() {
		if(!this.horasCalculadas)
			this.calculaHorasCompletadas();
		
		return this.horasEletivasCompletadas;
	}

	@Transient
	public int getHorasOpcionaisCompletadas() {
		if(!this.horasCalculadas)
			this.calculaHorasCompletadas();
		
		return this.horasOpcionaisCompletadas;
	}
	
	@Transient
	public int getSobraHorasEletivas()
	{
		if(!this.horasCalculadas)
			this.calculaHorasCompletadas();
		
		return this.sobraHorasEletivas;
	}
	
	@Transient
	public int getHorasAceConcluidas() {
		int aceCadastradas = 0;
		
		for(EventoAce e: this.listaEventosAce)
		{
			aceCadastradas += e.getHoras();
		}
		
		return Math.min((horasAceConcluidas + aceCadastradas), this.grade.getHorasAce());
	}
	
	@Transient
	public int getSobraHorasOpcionais()
	{
		if(!this.horasCalculadas)
			this.calculaHorasCompletadas();
		
		return this.sobraHorasOpcionais;
	}
	
	@Transient
	public void dadosAlterados()
	{
		this.horasCalculadas = false;
	}
}