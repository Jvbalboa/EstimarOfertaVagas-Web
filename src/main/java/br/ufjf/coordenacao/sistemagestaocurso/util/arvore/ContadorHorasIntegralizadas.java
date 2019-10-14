package br.ufjf.coordenacao.sistemagestaocurso.util.arvore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.ClassStatus;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.Disciplina;
import br.ufjf.coordenacao.sistemagestaocurso.model.EventoAce;
import br.ufjf.coordenacao.sistemagestaocurso.model.Grade;
import br.ufjf.coordenacao.sistemagestaocurso.repository.DisciplinaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.EventoAceRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.jpa.EntityManagerProducer;

public class ContadorHorasIntegralizadas implements Serializable{
	private static final long serialVersionUID = 1L;

	private Aluno aluno;
	private Grade grade;
	
	private Logger logger;

	@Inject
	private EntityManager manager;
	
	private DisciplinaRepository disciplinas;
	private EventoAceRepository eventosace;

	private int horasObrigatoriasCompletadas;
	private int horasEletivasCompletadas;
	private int sobraHorasEletivas;
	private int horasOpcionaisCompletadas;
	private int horasAceConcluidas;
	private int sobraHorasOpcionais;
	private boolean horasCalculadas; 

	public ContadorHorasIntegralizadas(Aluno aluno)
	{
		this.aluno = aluno;
		this.grade = aluno.getGrade();

		this.logger = Logger.getLogger(ContadorHorasIntegralizadas.class);
	}
	
	public int getHorasObrigatoriasCompletadas() {
		if(!this.horasCalculadas)
			this.calculaHorasCompletadas();
			
		return this.horasObrigatoriasCompletadas;
	}

	public int getHorasEletivasCompletadas() {
		if(!this.horasCalculadas)
			this.calculaHorasCompletadas();
		
		return this.horasEletivasCompletadas;
	}

	public int getHorasOpcionaisCompletadas() {
		if(!this.horasCalculadas)
			this.calculaHorasCompletadas();
		
		return this.horasOpcionaisCompletadas;
	}
	
	public int getSobraHorasEletivas()
	{
		if(!this.horasCalculadas)
			this.calculaHorasCompletadas();
		
		return this.sobraHorasEletivas;
	}
	
	public int getHorasAceConcluidas() {
		if(!this.horasCalculadas)
			this.calculaHorasCompletadas();
		
		int aceCadastradas = 0;
		
		List<EventoAce> eventos = this.eventosace.buscarPorMatricula(this.aluno.getMatricula());
		
		for(EventoAce evento: eventos)
		{
			aceCadastradas += evento.getHoras();
		}
				
		return Math.min((horasAceConcluidas + aceCadastradas), this.grade.getHorasAce());
	}
	
	public int getSobraHorasOpcionais()
	{
		if(!this.horasCalculadas)
			this.calculaHorasCompletadas();
		
		return this.sobraHorasOpcionais;
	}
	
	private void calculaHorasCompletadas()
	{
		EntityManagerProducer managerProducer = new EntityManagerProducer();
		this.manager = managerProducer.createEntityManager();
		this.disciplinas = new DisciplinaRepository(this.manager);
		this.eventosace = new EventoAceRepository(this.manager);
		
		logger.info("Calculando horas integralizadas pelo aluno " + this.aluno.getMatricula());
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
		Student st = sh.getStudents().get(this.aluno.getMatricula());
		
		if (st == null){
			logger.error("O aluno "+ this.aluno.getMatricula()+ " não foi encontrado na base de dados. Tarefa cancelada");
			FacesMessage msg = new FacesMessage("O aluno:" + this.aluno.getMatricula() + " não tem nenhum histórico de matricula cadastrado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;

		}
		
		HashMap<Class, ArrayList<String[]>> aprovado = new HashMap<Class, ArrayList<String[]>>(st.getClasses(ClassStatus.APPROVED));
		
		for(int i: cur.getMandatories().keySet())
		{
			for(Class c: cur.getMandatories().get(i))
			{
				if(aprovado.containsKey(c)){
					
					Disciplina d = disciplinas.buscarPorCodigoDisciplina(c.getId());
					if(d != null)
					{
						c.setWorkload(d.getCargaHoraria());
					}
					
					this.horasObrigatoriasCompletadas += c.getWorkload();
					aprovado.remove(c);
				}
				
				
			}
		}
		
		for(Class c: cur.getElectives())
			if(aprovado.containsKey(c))	{
				this.horasEletivasCompletadas += c.getWorkload();
				aprovado.remove(c);
			}
		
		Set<Class> ap = aprovado.keySet();
		Iterator<Class> i = ap.iterator();
		while(i.hasNext()){
			Class c = i.next();
			for(String[] s2: aprovado.get(c))	{
				if (s2[1].equals("APR") || s2[1].equals("A")){
					Disciplina d = disciplinas.buscarPorCodigoDisciplina(c.getId());
					
					if(d != null)
						c.setWorkload(d.getCargaHoraria());
					
					horasAceConcluidas += c.getWorkload();
				}
				else
				{
					Disciplina opcional = disciplinas.buscarPorCodigoDisciplina(c.getId());
					horasOpcionaisCompletadas += opcional.getCargaHoraria();
				}
			}
		}
		
		if(this.horasEletivasCompletadas > this.grade.getHorasEletivas())
		{
			this.sobraHorasEletivas = this.horasEletivasCompletadas - this.grade.getHorasEletivas();
			this.horasEletivasCompletadas -= this.sobraHorasEletivas;
			this.horasOpcionaisCompletadas += this.sobraHorasEletivas;
		}
		
		if(this.horasOpcionaisCompletadas > this.grade.getHorasOpcionais())
		{
			this.sobraHorasOpcionais = this.horasOpcionaisCompletadas - this.grade.getHorasOpcionais();
			this.horasOpcionaisCompletadas -= this.sobraHorasOpcionais;
		}
		
		this.horasCalculadas = true;
		logger.info("Horas calculadas com sucesso.");
		
		
	}
	
	public void dispose()
	{
		try {
			this.manager.close();
			this.disciplinas = null;
		} catch (Exception e) {
			logger.warn("Ocorreu um problema ao fechar os recursos" + e.getMessage());
		}
	}
}
