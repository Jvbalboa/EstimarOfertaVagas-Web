package br.ufjf.coordenacao.sistemagestaocurso.model;

import java.util.ArrayList;
import java.util.Collection;
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

import org.apache.poi.ss.formula.functions.T;

import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.ClassStatus;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.SituacaoDisciplina;
import br.ufjf.coordenacao.sistemagestaocurso.repository.DisciplinaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.EventoAceRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.*;

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
	private List<IRA> iras;
	private int horasObrigatoriasCompletadas;
	private int horasEletivasCompletadas;
	private List<Disciplina> disciplinasEletivasCompletadas;
	private List<String> codigoEletivasExtras;
	private int sobraHorasEletivas;
	private int horasOpcionaisCompletadas;
	private int horasAceConcluidas;
	private List<Disciplina> disciplinasOpcionaisCompletadas;
	private List<String> codigoOpcionaisExtras;
	private int sobraHorasOpcionais;

	private boolean horasCalculadas;
	
	private DisciplinaRepository disciplinaRepository;
	private EventoAceRepository eventoAceRepository;

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
	
	@OneToMany(mappedBy = "aluno", targetEntity = IRA.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<IRA> getIras(){
		return iras;
	}

	@OneToMany(mappedBy = "aluno", targetEntity = Historico.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Historico> getGrupoHistorico() {
		return grupoHistorico;
	}
	

	@Transient
	public List<Historico> getGrupoHistorico(String statusDisciplina) {
		List<Historico> historicos = new ArrayList<Historico>();
		for(Historico h : this.getGrupoHistorico()) {
			if(statusDisciplina.equals(h.getStatusDisciplina()))
				historicos.add(h);
		}
		return historicos;
	}

	public void setGrupoHistorico(List<Historico> grupoHistorico) {
		this.grupoHistorico = grupoHistorico;
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

	@Column(name="IRA")
	public Float getIra() {
		return ira;
	}


	public void setIra(Float ira) {
		this.ira = ira;
	}

	public int periodoCorrente(String ingresso,String semestreAtual){
		
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
		this.disciplinasOpcionaisCompletadas = new ArrayList<Disciplina>();
		this.disciplinasEletivasCompletadas = new ArrayList<Disciplina>();
		
		this.horasObrigatoriasCompletadas = 0;
		this.horasEletivasCompletadas = 0;
		this.horasOpcionaisCompletadas = 0;
		this.sobraHorasEletivas = 0;
		this.horasCalculadas = false;
		
		ImportarArvore importador;
		EstruturaArvore estruturaArvore = EstruturaArvore.getInstance();
		importador = estruturaArvore.recuperarArvore(this.grade,false);
		
		Curriculum cur = importador.get_cur();
		StudentsHistory sh = importador.getSh();		
		Student st = sh.getStudents().get(this.getMatricula());
		
		if (st == null){
			FacesMessage msg = new FacesMessage("O aluno:" + this.getMatricula() + " não tem nenhum histórico de matricula cadastrado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		HashMap<Class, ArrayList<String[]>> aprovado = new HashMap<Class, ArrayList<String[]>>(st.getClasses(ClassStatus.APPROVED));
		
		//++
		List<Disciplina> disciplinas = disciplinaRepository.listarTodos();
				
		for(int i: cur.getMandatories().keySet())
		{
			for(Class c: cur.getMandatories().get(i))
			{
				if(aprovado.containsKey(c)){
					Disciplina d = null;
					for(Disciplina disciplina: disciplinas) {
						if(disciplina.getCodigo().equals(c.getId())) {
							d = disciplina;
						}
					}
						
					if(d != null)
					{
						c.setWorkload(d.getCargaHoraria());
					}
					
					this.horasObrigatoriasCompletadas += c.getWorkload();
					aprovado.remove(c);
				}				
			}
		}
		
		for(Class c: cur.getElectives()) {
			if(aprovado.containsKey(c))	{
				this.horasEletivasCompletadas += c.getWorkload();
				Disciplina d = null;
				for(Disciplina disciplina: disciplinas) {
					if(disciplina.getCodigo().equals(c.getId())) {
						d = disciplina;
						this.disciplinasEletivasCompletadas.add(d);
					}
				}
				
				aprovado.remove(c);
			}
		}
		
		Set<Class> ap = aprovado.keySet();
		Iterator<Class> i = ap.iterator();
		while(i.hasNext()){
			Class c = i.next();
			for(String[] s2: aprovado.get(c))	{
				if (s2[1].equals("APR") || s2[1].equals("A")){
					Disciplina d = null;
					for(Disciplina disciplina: disciplinas) {
						if(disciplina.getCodigo().equals(c.getId())) {
							d = disciplina;
						}
					}
					
					if(d != null)
						c.setWorkload(d.getCargaHoraria());
					horasAceConcluidas += c.getWorkload();
				}
				else
				{
					Disciplina opcional = null;
					
					for(Disciplina disciplina: disciplinas) {
						if(disciplina.getCodigo().equals(c.getId())) {
							opcional = disciplina;
							this.disciplinasOpcionaisCompletadas.add(opcional);
						}
					}
					horasOpcionaisCompletadas += opcional.getCargaHoraria();
				}
			}
		}

		if(this.horasEletivasCompletadas > this.grade.getHorasEletivas())
		{
			this.sobraHorasEletivas = this.horasEletivasCompletadas - this.grade.getHorasEletivas();
			this.horasEletivasCompletadas -= this.sobraHorasEletivas;
			this.horasOpcionaisCompletadas += this.sobraHorasEletivas;
			
			//--
			this.codigoEletivasExtras = this.selecionaExcedentes(this.sobraHorasEletivas, this.disciplinasEletivasCompletadas);
		}
		
		if(this.horasOpcionaisCompletadas > this.grade.getHorasOpcionais())
		{
			this.sobraHorasOpcionais = this.horasOpcionaisCompletadas - this.grade.getHorasOpcionais();
			this.horasOpcionaisCompletadas -= this.sobraHorasOpcionais;
			this.horasAceConcluidas += this.sobraHorasOpcionais;
			
			//--
			this.codigoOpcionaisExtras = this.selecionaExcedentes(this.sobraHorasOpcionais, this.disciplinasOpcionaisCompletadas);
		}
		//-----------------------------
		this.horasCalculadas = true;
	}
	
	@Transient
	private ArrayList<String> selecionaExcedentes(int horasExcedentes, List<Disciplina> disciplinasCompletadas){
		ArrayList<String> solucao = new ArrayList<String>(); // lista com codigo das disciplinas que vao ser selecionadas como excedentes
		int tamanhoConjunto = disciplinasCompletadas.size(); // tamanho do conjunto das disciplinas
		boolean[][] tabela = new boolean[tamanhoConjunto + 1][horasExcedentes+1]; // tabela auxiliar
		int[] pesos = new int[tamanhoConjunto + 1]; // vetor contendo as horas de cada disciplina
		
		
		// atribuindo os valores dos pesos de acordo com a carga horária da disciplina
		for(int i = 1; i <= tamanhoConjunto; i++) {
			pesos[i] = disciplinasCompletadas.get(i-1).getCargaHoraria();
			// inserindo a primeira coluna
			tabela[i][0] = true;
		}
		tabela[0][0] = true;
		
		//inicio do subset sum
		boolean s;
		for(int j = 1; j < horasExcedentes+1; j++) {
			//zerando a primeira linha
			tabela[0][j] = false;
			
			for(int i = 1; i < tamanhoConjunto+1; i++) {
				//se já tiver conseguido essa subsoma s == 1
				s = tabela[i-1][j];
				
				//se ainda nao tiver conseguido a subsoma e este elemento for viavel
				if(s == false && pesos[i] <= j) {
					s = tabela[i-1][j-pesos[i]];
				}
				
				tabela[i][j] = s;
			}
		}
		
		// encontra melhor solução
		solucao = this.encontraSolucao(tabela, pesos, tamanhoConjunto, horasExcedentes, disciplinasCompletadas, solucao);

		return solucao;
	}
	
	@Transient
	private ArrayList<String> encontraSolucao(boolean tabela[][], int pesos[], int tamanhoConjunto, int horasExcedentes, List<Disciplina> disciplinasCompletadas, ArrayList<String> solucao){
		if(horasExcedentes == 0) {
			return solucao;
		}
		else {
			for(int i = 0; i < tamanhoConjunto; i++) {
				if(tabela[i][horasExcedentes]) {
					solucao.add(disciplinasCompletadas.get(i-1).getCodigo());
					return this.encontraSolucao(tabela, pesos, tamanhoConjunto, horasExcedentes - pesos[i], disciplinasCompletadas, solucao);
				}
			}
		}
		return solucao;
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
		if(!this.horasCalculadas)
			this.calculaHorasCompletadas();
		
		int aceCadastradas = 0;
		
		List<EventoAce> eventos = this.eventoAceRepository.buscarPorMatricula(this.getMatricula());
		
		for(EventoAce evento: eventos)
		{
			aceCadastradas += evento.getHoras();
		}
				
		this.horasAceConcluidas =  Math.min((horasAceConcluidas + aceCadastradas), this.grade.getHorasAce());
		
		return this.horasAceConcluidas;
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


	public void setIras(List<IRA> iras) {
		this.iras = iras;
	}
	
	@Transient
	public List<SituacaoDisciplina> getExcedenteEletivas() {  // cria uma situação disciplina com  a carga horária igual as horas extras eletivas
		List<SituacaoDisciplina> eletivasExcedentes = new ArrayList<SituacaoDisciplina>();
		for(String codigoEletiva : this.codigoEletivasExtras) {
			Disciplina disciplina = this.disciplinaRepository.buscarPorCodigoDisciplina(codigoEletiva);
			SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
			disciplinaSituacao.setCodigo(disciplina.getCodigo());
			disciplinaSituacao.setSituacao("APROVADO");
			disciplinaSituacao.setCargaHoraria(disciplina.getCargaHoraria() + "");
			disciplinaSituacao.setNome(disciplina.getNome() + " : Excedente Eletiva");
			eletivasExcedentes.add(disciplinaSituacao);
		}
		return eletivasExcedentes;
	}
	
	@Transient
	public List<EventoAce> getExcedenteOpcionais() { // tp
		List<EventoAce> opcionaisExcedentes = new ArrayList<EventoAce>();
		for(String eletivaExtra : this.codigoOpcionaisExtras) {
			for(Disciplina d : this.disciplinasOpcionaisCompletadas) {
				if(d.getCodigo() == eletivaExtra) {
					EventoAce evento = new EventoAce();
					evento.setDescricao(d.getCodigo() + " " + d.getNome() + " : Disciplina Excedente Opcional");
					evento.setHoras((long) d.getCargaHoraria());
					evento.setExcluir(false);
					opcionaisExcedentes.add(evento);
				}
			}
		}
		
		return opcionaisExcedentes;
	}
	
	@Transient
	public void setEventoAceRepository(EventoAceRepository eventoAceRepository) {
		this.eventoAceRepository = eventoAceRepository;
	}
	
	@Transient
	public void setDisciplinaRepository(DisciplinaRepository disciplinaRepository) {
		this.disciplinaRepository = disciplinaRepository;
	}
}