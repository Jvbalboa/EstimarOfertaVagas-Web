package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.repository.AlunoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.EventoAceRepository;

@Named
@ViewScoped
public class AlunosAptosEstagioController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Curso curso = new Curso();
	private List<Aluno> listaAlunos = new ArrayList<Aluno>();
	private List<Aluno> listaAlunosNaoEstaoEmAcompanhamento = new ArrayList<Aluno>();
	private List<Aluno> listaAlunosIraMaiorSessentaUltimosTresSemestres = new ArrayList<Aluno>();
	private boolean listaFoiFiltrada = false;
	private boolean listaSendoExibida = false;
	private int numeroAlunosListados;
	
	@Inject
	private AlunoRepository alunoRepository;
	@Inject
	private EventoAceRepository eventoAceRepository;
	@Inject
	private UsuarioController usuarioController;
	
	@PostConstruct
	public void init() {
		usuarioController.atualizarPessoaLogada();
		curso = usuarioController.getAutenticacao().getCursoSelecionado();
		listaAlunos = alunoRepository.buscarTodosAlunoCursoObjeto(curso.getId());
	}
	
	public void exibirAlunosNaoEstaoEmAcompanhamento() {
		if (!this.listaFoiFiltrada) {
			this.filtrarListaAlunos();
		}
		
		this.listaAlunos = this.listaAlunosNaoEstaoEmAcompanhamento;
		
		this.numeroAlunosListados = this.listaAlunos.size();
		this.setListaSendoExibida(true);
	}
	
	public void exibirAlunosIraMaiorSessentaUltimosTresSemestres() {
		if (!this.listaFoiFiltrada) {
			this.filtrarListaAlunos();
		}
		
		this.listaAlunos = this.listaAlunosIraMaiorSessentaUltimosTresSemestres;
		
		this.numeroAlunosListados = this.listaAlunos.size();
		this.setListaSendoExibida(true);
	}
	
	public void filtrarListaAlunos() {
		for (Aluno aluno : this.listaAlunos) {
			aluno.setEventoAceRepository(eventoAceRepository);
			
			aluno.buscarUltimosTresSemestres();
			if (aluno.getUltimosTresSemestres().size() == 3) {
				
				aluno.verificarAcompanhamentoAcademico();
				if (!aluno.isEmAcompanhamentoAcademico()) {
					this.listaAlunosNaoEstaoEmAcompanhamento.add(aluno);
				}
				
				aluno.calcularIraUltimosTresSemestres();
				boolean iraMaiorSessentaUltimosTresSemestres = true;
				for (float ira : aluno.getIraUltimosTresSemestres()) {
					if (ira < 60) {
						iraMaiorSessentaUltimosTresSemestres = false;
					}
				}
				
				if (iraMaiorSessentaUltimosTresSemestres) {
					this.listaAlunosIraMaiorSessentaUltimosTresSemestres.add(aluno);
				}
			}
		}
		
		this.listaFoiFiltrada = true;
	}
	
	public List<Aluno> getlistaAlunos() {
		return listaAlunos;
	}
	
	public void setListaAlunos(List<Aluno> listaAlunos) {
		this.listaAlunos = listaAlunos;
	}

	public boolean isListaSendoExibida() {
		return listaSendoExibida;
	}

	public void setListaSendoExibida(boolean listaSendoExibida) {
		this.listaSendoExibida = listaSendoExibida;
	}

	public int getNumeroAlunosListados() {
		return numeroAlunosListados;
	}

	public void setNumeroAlunosListados(int numeroAlunosListados) {
		this.numeroAlunosListados = numeroAlunosListados;
	}
}
