package br.ufjf.coordenacao.sistemagestaocurso.controller;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufjf.coordenacao.OfertaVagas.estimate.Estimator;
import br.ufjf.coordenacao.OfertaVagas.model.*;
import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.*;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.EspectativaDisciplina;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.GradeHistorico;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.EstruturaArvore;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.ImportarArvore;

import org.primefaces.component.datatable.DataTable;




@Named
@ViewScoped
public class VisaoGeralController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;
	private int i;
	private ImportarArvore importador;
	private Curriculum curriculum;
	private Aluno aluno = new Aluno();
	private Curso curso = new Curso();
	private boolean ldGridHistorico = false;
	private int percNrMatriculados = 90  ;
	private int percNrRN = 60 ;
	private int percNrRF = 70;
	private int percNrH = 80;
	private int percNrQH  = 50;
	private List<String> selectedOptionsObrigatoria = new ArrayList<String>();
	private List<String> selectedOptionsEletiva = new ArrayList<String>();
	private List<String> selectedOptionsOpcional = new ArrayList<String>();
	private List<String> selectedOptionsPeriodo = new ArrayList<String>();
	private List<String> selectedOptionsPeriodoDois = new ArrayList<String>();
	private List<String> selectedOptionsPeriodoTres = new ArrayList<String>();
	private List<String> selectedOptionsSituacao = new ArrayList<String>();
	private List<String> selectedOptionsSituacaoDois = new ArrayList<String>();
	private List<String> selectedOptionsSituacaoTres = new ArrayList<String>();
	private List<String> selectedOptionsSituacaoQuatro = new ArrayList<String>();
	private List<String> listagrade = new ArrayList<String>();
	private List<String> selectedOptionsEstimativa = new ArrayList<String>();
	private ArrayList<String> lista = new ArrayList<String>();
	private ArrayList<String> listaDisciplinasDisponiveis = new ArrayList<String>();  
	private ArrayList<String> listaTodasDisciplinasDisponiveis = new ArrayList<String>(); 
	private ArrayList<String> listaTodasDisciplinasSelecionadas = new ArrayList<String>(); 
	private ArrayList<String> listaAlunosSelecionados = new ArrayList<String>(); 
	private ArrayList<String> listaAnosSelecionados = new ArrayList<String>();
	private ArrayList<String> listaAnosSelecionadosInvertida = new ArrayList<String>();
	private ArrayList<String> listaAlunosTodos = new ArrayList<String>();
	private ArrayList<String> listaAnoTodos = new ArrayList<String>();
	private List<ColumnModel> columns;
	private List<Aluno> listaAluno= new ArrayList<Aluno>();	
	private ArrayList<Class> classes = new ArrayList<Class>();
	private List<Historico> listaHistorico= new ArrayList<Historico>();
	private List<GradeHistorico> listaGradeHistorico ;
	private GradeHistorico selecionadoGradeHistorico;
	private List<GradeHistorico> listaGradeHistoricoSelecionadas ;
	private List<EspectativaDisciplina> listaEspectativaDisciplina = new ArrayList<EspectativaDisciplina>();	
	private List<GradeDisciplina> listagradeDisciplina = new ArrayList<GradeDisciplina>();
	private GradeHistorico gradeHistoricoSelecionado;
	private List<EspectativaDisciplina> listaEspectativaDisciplinaSelecionada;
	private boolean lgPeriodoUm = false;
	private boolean lgPeriodoCinco = false;
	private boolean lgPeriodoNove = false;
	private boolean lgEletivas = false;
	private boolean lgEstimativa = true;
	private boolean lgPlanejado = true;
	private boolean lgBarra = true;
	private Grade grade;
	private EstruturaArvore estruturaArvore;
	

	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init() {

		estruturaArvore = EstruturaArvore.getInstance();
		usuarioController.atualizarPessoaLogada();
		curso = usuarioController.getAutenticacao().getCursoSelecionado();		
	}


	//========================================================= METODOS ==================================================================================//


	public void dadosArvore(){
		for(String codigoGrade :listagrade){
			for(Grade gradeSelecionada:curso.getGrupoGrades() ){				
				if (gradeSelecionada.getCodigo().equals(codigoGrade)){
					grade = gradeSelecionada;	
					break;
				}				
			}			
			importador = estruturaArvore.recuperarArvore(grade,true);
			curriculum = importador.get_cur();
			printTableHeader();
		}
	}


	public void gerarDados(){

		listaEspectativaDisciplina = new ArrayList<EspectativaDisciplina>();
		classes = new ArrayList<Class>();
		listaDisciplinasDisponiveis = new ArrayList<String>(); 
		columns = new ArrayList<ColumnModel>();
		listaGradeHistorico = new ArrayList<GradeHistorico>();
		listaTodasDisciplinasDisponiveis = new ArrayList<String>();  
		listaAlunosTodos = new ArrayList<String>();  
		listaAnoTodos = new ArrayList<String>();  
		boolean todos = false;
		boolean todosituacao = false;
		//desativar tabela quando nao tiver nenhuma grade selecionada
		if (listagrade == null  || listagrade.size() == 0 ){
			ldGridHistorico = false;
			return;
		}
		//se nao tiver nenhuma situacao selecionada considera que todas estao selecionadas
		if(	selectedOptionsSituacao.size() == 0 && selectedOptionsSituacaoDois.size() == 0 && selectedOptionsSituacaoTres.size() == 0 && selectedOptionsSituacaoQuatro.size() == 0){
			todosituacao = true;
		}
		//se nao tiver nenhuma selecao lista todos
		if (	listagrade.size() == 1 &&
				(listaAlunosSelecionados == null || listaAlunosSelecionados.size() == 0) && 
				(listaAnosSelecionados == null || listaAnosSelecionados.size() == 0) && 
				todosituacao && 
				(listaAnosSelecionadosInvertida == null || listaAnosSelecionadosInvertida.size() == 0)){
			todos = true;
		}


		dadosArvore();

		for(String codigoGrade :listagrade){			

			for(Grade gradeSelecionada:curso.getGrupoGrades() ){				
				if (gradeSelecionada.getCodigo().equals(codigoGrade)){
					grade = gradeSelecionada;	
					break;
				}				
			}				
			importador = estruturaArvore.recuperarArvore(grade,true);
			StudentsHistory sh = importador.getSh();
			ArrayList<String> student = new ArrayList<String>();
			student.addAll(sh.getStudents().keySet());
			Collections.sort(student);
			String colorCode = "";	
			for (String stid : student) {
				boolean naoGravar = false;
				Student st = sh.getStudents().get(stid);
				String anoIngresso = (st.getFirstSemester() + "").substring(0,4);
				listaAlunosTodos.add(stid);
				if( listaAnoTodos != null && !listaAnoTodos.contains(anoIngresso)               ){
					listaAnoTodos.add(anoIngresso);
				}

				if (listaAnosSelecionados != null){
					if ( listaAnosSelecionados.size() != 0  ){
						if ( !listaAnosSelecionados.contains(anoIngresso)             ){
							continue;
						}
					}
				}

				if (listaAnosSelecionadosInvertida != null){
					if ( listaAnosSelecionadosInvertida.size() != 0  ){
						if ( listaAnosSelecionadosInvertida.contains(anoIngresso)             ){
							continue;
						}
					}
				}

				if (listaAlunosSelecionados != null){
					if ( listaAlunosSelecionados.size() != 0  ){
						if (!listaAlunosSelecionados.contains(stid)){
							continue;
						}
					}
				}

				//imprimir opcionais
				String optional = "Opcionais:Aprovado - ";
				String disciplinasOpcionais = "";
				for (Class class1 : st.getClasses(ClassStatus.APPROVED).keySet()) {
					boolean achou = false;
					for (Class classOpcional :classes ) {
						if (classOpcional.getId().equals(class1.getId())){
							achou = true;
							break;
						}
					}
					if (achou == false){
						optional += class1.getId() + "; ";
					}
				}
				if (!optional.equals("Opcionais:Aprovado - "))
					disciplinasOpcionais = disciplinasOpcionais + (optional.substring(0, optional.length()-1)+". ");

				optional = "Matriculado - ";
				for (Class class1 : st.getClasses(ClassStatus.ENROLLED).keySet()) {
					boolean achou = false;
					for (Class classOpcional :classes ) {
						if (classOpcional.getId().equals(class1.getId())){
							achou = true;
							break;
						}
					}
					if (achou == false){
						optional += class1.getId() + "; ";
					}
				}
				if (!optional.equals("Matriculado - "))
					disciplinasOpcionais = disciplinasOpcionais + (optional.substring(0, optional.length()-1)+". ");

				for (Class c1 : classes) {

					int retorno = Estimator.processStudentCourseStatus(ClassFactory.getClass(st.getCourse(), st.getCurriculum(), c1.getId()), st);
					if(listaDisciplinasDisponiveis.contains(c1.getId())){
						if (retorno == 0) 		colorCode = "#0B2161";
						else if (retorno == 1) 	colorCode = "#7777FF";
						else if (retorno == 2)  colorCode = "#FFA500";
						else if (retorno ==3)	colorCode = "#FF0000";
						else if (retorno == 4)	colorCode = "#04B431";
						else if (retorno == 5)	colorCode = "#FA00FF";
						else colorCode = "#FFFFFF"; // Não pode cursar
						if(((selectedOptionsSituacao.contains(colorCode) || selectedOptionsSituacaoDois.contains(colorCode) 
								|| selectedOptionsSituacaoTres.contains(colorCode) || selectedOptionsSituacaoQuatro.contains(colorCode)) == false)
								&& todos == false && todosituacao == false ){
							naoGravar = true;
							break;
						}
						lista.add(colorCode);
					}
				}

				if(!naoGravar){
					listaGradeHistorico.add(new GradeHistorico(lista, disciplinasOpcionais,sh.getStudents().get(stid).getNome(),stid));
				}
				lista = new ArrayList<String>();
			}
		}
		int i = 0;
		if (listaGradeHistorico.size() != 0){
			for (i=0;i< listaGradeHistorico.get(0).getHistoricoAluno().size() ;i++){
				for(GradeHistorico gradeHistorico:listaGradeHistorico){
					if (gradeHistorico.getHistoricoAluno().get(i) == "#7777FF") 		listaEspectativaDisciplina.get(i).setNrMatriculados(listaEspectativaDisciplina.get(i).getNrMatriculados() + 1);
					else if (gradeHistorico.getHistoricoAluno().get(i) == "#FFA500") 		listaEspectativaDisciplina.get(i).setNrRN(listaEspectativaDisciplina.get(i).getNrRN() + 1);
					else if (gradeHistorico.getHistoricoAluno().get(i) == "#FF0000") 		listaEspectativaDisciplina.get(i).setNrRF(listaEspectativaDisciplina.get(i).getNrRF() + 1);
					else if (gradeHistorico.getHistoricoAluno().get(i) == "#04B431") 		listaEspectativaDisciplina.get(i).setNrH(listaEspectativaDisciplina.get(i).getNrH() + 1);
					else if (gradeHistorico.getHistoricoAluno().get(i) == "#FA00FF") 		listaEspectativaDisciplina.get(i).setNrQH(listaEspectativaDisciplina.get(i).getNrQH() + 1);
				}
			}
		}
		calculaPercentual();
		if (listaGradeHistorico.size() != 0 ){
			ldGridHistorico = true;
		}
	}

	public void calculaPercentual(){

		for (EspectativaDisciplina espectativaDisciplina:listaEspectativaDisciplina){
			espectativaDisciplina.setPercNrMatriculados( espectativaDisciplina.getNrMatriculados() * percNrMatriculados / 100	);
			espectativaDisciplina.setPercNrRN( espectativaDisciplina.getNrRN() * percNrRN / 100	);
			espectativaDisciplina.setPercNrRF( espectativaDisciplina.getNrRF() * percNrRF / 100	);
			espectativaDisciplina.setPercNrH( espectativaDisciplina.getNrH() * percNrH / 100	);
			espectativaDisciplina.setPercNrQH( espectativaDisciplina.getNrQH() * percNrQH / 100	);
			espectativaDisciplina.setNrTotal(espectativaDisciplina.getNrMatriculados() + espectativaDisciplina.getNrRN() + espectativaDisciplina.getNrRF() + 
					espectativaDisciplina.getNrH() + espectativaDisciplina.getNrQH());
			espectativaDisciplina.setPercNrTotal(espectativaDisciplina.getPercNrMatriculados() + espectativaDisciplina.getPercNrRN() + espectativaDisciplina.getPercNrRF()
					+ espectativaDisciplina.getPercNrH() + espectativaDisciplina.getPercNrQH());
		}
	}

	public void printTableHeader() {

		Class[] classe = { };
		//se tiver so uma grade
		if (listagrade.size() == 1){

			ArrayList<Integer> semester = new ArrayList<Integer>();
			semester.addAll(curriculum.getMandatories().keySet());
			Collections.sort(semester);
			columns = new ArrayList<ColumnModel>();
			i = 0;
			String corDisciplia = "#A9E2F3";


			for (Integer sem : semester) {
				for (Class c1 : curriculum.getMandatories().get(sem).toArray(classe)) {
					listaTodasDisciplinasDisponiveis.add(c1.getId());
					//traz todas disciplinas quanto nao tiver nenhuma selecionada
					if(listaTodasDisciplinasSelecionadas != null && listaTodasDisciplinasSelecionadas.size() > 0  ){
						if(listaTodasDisciplinasSelecionadas.contains(c1.getId())){
							listaDisciplinasDisponiveis.add(c1.getId());
							classes.add(ClassFactory.getClass(curso.getCodigo(), curriculum.getCurriculumId(), c1.getId()));
							columns.add(new ColumnModel(c1.getId(), i, corDisciplia));
							EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
							espectativaDisciplina.setNomeDisciplina(c1.getId());							
							listaEspectativaDisciplina.add(espectativaDisciplina);
							i++;

							if (corDisciplia.equals("#A9E2F3")){
								corDisciplia = "#CEECF5";
							}
							else {
								corDisciplia = "#A9E2F3";
							}
						}
					}

					//se tiver alguma disciplina selecionada
					else  if (selectedOptionsPeriodo.size() != 0	||  selectedOptionsPeriodoDois.size() != 0	||  selectedOptionsPeriodoTres.size() != 0 ){

						//Disciplina disciplina = disciplinaDAO.buscarPorCodigoDisciplina(c1.getId());
						GradeDisciplina gradedisciplina = null;
						for(GradeDisciplina gradeDisciplinaSelecionada : grade.getGrupoGradeDisciplina()){							
							if (gradeDisciplinaSelecionada.getDisciplina().getCodigo().equals(c1.getId())){
								gradedisciplina = gradeDisciplinaSelecionada;								
							}			
						}
						String periodo = String.valueOf(gradedisciplina.getPeriodo());

						if (gradedisciplina.getPeriodo() < 10){
							periodo = "0" + periodo;
						}
						//verificar se tem algum periodo selecionado se tiver
						if ( selectedOptionsPeriodo.contains(periodo)  || selectedOptionsPeriodoDois.contains(periodo) || selectedOptionsPeriodoTres.contains(periodo)	){
							listaDisciplinasDisponiveis.add(c1.getId());
							classes.add(c1);
							columns.add(new ColumnModel(c1.getId(), i, corDisciplia));
							EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
							espectativaDisciplina.setNomeDisciplina(c1.getId());							
							listaEspectativaDisciplina.add(espectativaDisciplina);
							i++;
							if (corDisciplia.equals("#A9E2F3")){
								corDisciplia = "#CEECF5";
							}
							else {
								corDisciplia = "#A9E2F3";
							}
						}			
					}
					// verificar se tem algum tipo de disciplina selecionado
					else if (selectedOptionsEletiva == null || selectedOptionsEletiva.size() == 0){
						listaDisciplinasDisponiveis.add(c1.getId());
						classes.add(c1);
						columns.add(new ColumnModel(c1.getId(), i, corDisciplia));
						EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
						espectativaDisciplina.setNomeDisciplina(c1.getId());							
						listaEspectativaDisciplina.add(espectativaDisciplina);
						i++;
						if (corDisciplia.equals("#A9E2F3")){
							corDisciplia = "#CEECF5";
						}
						else {
							corDisciplia = "#A9E2F3";
						}
					}
				}
			}

			//Imprimindo disciplinas eletivas
			for(Class c1 : curriculum.getElectives()) {
				listaTodasDisciplinasDisponiveis.add(c1.getId());
				if(listaTodasDisciplinasSelecionadas != null && listaTodasDisciplinasSelecionadas.size() > 0  ){
					if(listaTodasDisciplinasSelecionadas.contains(c1.getId())){
						listaDisciplinasDisponiveis.add(c1.getId());
						classes.add(c1);
						columns.add(new ColumnModel(c1.getId(), i,"#ccc"));
						EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
						espectativaDisciplina.setNomeDisciplina(c1.getId());							
						listaEspectativaDisciplina.add(espectativaDisciplina);
						i++;
					}
				}
				//verificar se alguma disciplina foi selecionada
				else if (selectedOptionsEletiva.contains("Eletivas")	){
					listaDisciplinasDisponiveis.add(c1.getId());
					classes.add(c1);
					columns.add(new ColumnModel(c1.getId(), i,"#ccc"));
					EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
					espectativaDisciplina.setNomeDisciplina(c1.getId());							
					listaEspectativaDisciplina.add(espectativaDisciplina);
					i++;
				}
				//verifica se tem algum periodo selecionado
				else  if ( (selectedOptionsPeriodo == null || selectedOptionsPeriodo.size() == 0) 
						&& (selectedOptionsPeriodoDois == null || selectedOptionsPeriodoDois.size() == 0 ) 
						&& (selectedOptionsPeriodoTres == null || selectedOptionsPeriodoTres.size() == 0) )	{

					listaDisciplinasDisponiveis.add(c1.getId());
					classes.add(c1);
					columns.add(new ColumnModel(c1.getId(), i,"#ccc"));
					EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
					espectativaDisciplina.setNomeDisciplina(c1.getId());							
					listaEspectativaDisciplina.add(espectativaDisciplina);
					i++;
				}
			}
		}

		else if (listagrade.size() > 1){
			i = 0;
			ArrayList<Integer> semester = new ArrayList<Integer>();
			semester.addAll(curriculum.getMandatories().keySet());
			Collections.sort(semester);

			//Imprimindo obrigatórias
			for (Integer sem : semester) {
				for (Class c1 : curriculum.getMandatories().get(sem).toArray(classe)) {
					//verifica se disciplina ja foi incluida
					if (!listaTodasDisciplinasDisponiveis.contains(c1.getId())){
						listaTodasDisciplinasDisponiveis.add(c1.getId());
					}
					//verifica se alguma disciplina esta selecionada
					if(listaTodasDisciplinasSelecionadas != null && listaTodasDisciplinasSelecionadas.size() > 0  ){
						if(listaTodasDisciplinasSelecionadas.contains(c1.getId()) && !listaDisciplinasDisponiveis.contains(c1.getId())){
							listaDisciplinasDisponiveis.add(c1.getId());
							classes.add(c1);
							columns.add(new ColumnModel(c1.getId(), i,""));
							EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
							espectativaDisciplina.setNomeDisciplina(c1.getId());							
							listaEspectativaDisciplina.add(espectativaDisciplina);
							i++;
						}
					}
					else if(!listaDisciplinasDisponiveis.contains(c1.getId())){
						listaDisciplinasDisponiveis.add(c1.getId());
						classes.add(c1);
						columns.add(new ColumnModel(c1.getId(), i, ""));
						EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
						espectativaDisciplina.setNomeDisciplina(c1.getId());							
						listaEspectativaDisciplina.add(espectativaDisciplina);
						i++;
					}			
				}
			}

			//Imprimindo eletivas
			for(Class c1 : curriculum.getElectives()) {
				if (!listaTodasDisciplinasDisponiveis.contains(c1.getId())){
					listaTodasDisciplinasDisponiveis.add(c1.getId());
				}
				if(listaTodasDisciplinasSelecionadas != null && listaTodasDisciplinasSelecionadas.size() > 0  ){
					if(listaTodasDisciplinasSelecionadas.contains(c1.getId()) && !listaDisciplinasDisponiveis.contains(c1.getId())){
						listaDisciplinasDisponiveis.add(c1.getId());
						classes.add(c1);
						columns.add(new ColumnModel(c1.getId(), i,""));
						EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
						espectativaDisciplina.setNomeDisciplina(c1.getId());							
						listaEspectativaDisciplina.add(espectativaDisciplina);
						i++;
					}
				}
				else if(!listaDisciplinasDisponiveis.contains(c1.getId())){
					listaDisciplinasDisponiveis.add(c1.getId());
					classes.add(c1);
					columns.add(new ColumnModel(c1.getId(), i,""));
					EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
					espectativaDisciplina.setNomeDisciplina(c1.getId());							
					listaEspectativaDisciplina.add(espectativaDisciplina);
					i++;
				}
			}
		}		
	}

	public List<String> gradeCodigos(String codigo) {
		codigo = codigo.toUpperCase();
		List<String> todos = new ArrayList<String>();
		for (Grade gradeQuestao : curso.getGrupoGrades()){
			if(gradeQuestao.getCodigo().contains(codigo)){
				todos.add(gradeQuestao.getCodigo()); 
			}
		}
		return todos;
	}

	public List<String> nomeDisciplina(String codigo) {
		List<String> listaSelecionadas = new ArrayList<String>();
		for (String disciplina:listaTodasDisciplinasDisponiveis ){
			if (disciplina.indexOf(codigo.toUpperCase()) >= 0){
				listaSelecionadas.add(disciplina);
			}
		}
		Collections.sort(listaSelecionadas);
		return listaSelecionadas;
	}	


	public List<String> nomeAluno(String codigo) {
		List<String> listaSelecionadas = new ArrayList<String>();
		for (String aluno:listaAlunosTodos ){
			if (aluno.indexOf(codigo.toUpperCase()) >= 0){
				listaSelecionadas.add(aluno);
			}
		}
		Collections.sort(listaSelecionadas);
		return listaSelecionadas;
	}

	public List<String> alunoAno(String codigo) {
		List<String> listaSelecionadas = new ArrayList<String>();
		for (String alunoAno:listaAnoTodos ){
			if (alunoAno.indexOf(codigo.toUpperCase()) >= 0){
				listaSelecionadas.add(alunoAno);
			}
		}
		Collections.sort(listaSelecionadas);
		return listaSelecionadas;
	}	

	public void onItemSelectGrade(int i) { 

		if (i==1){   // foi selecionado um ingresso
			listaAnosSelecionadosInvertida = new ArrayList<String>();
		}
		else if (i==2){ // foi selecionado um nao ingresso
			listaAnosSelecionados = new ArrayList<String>();
		}
		//tratamento para nao deixar selecionar uma mesma grade novamente
		if (listagrade != null && listagrade.size() > 0 && listagrade.indexOf(listagrade.get((listagrade.size() - 1 ))) != (listagrade.size() - 1) && listagrade.size() != 1){
			FacesMessage msg = new FacesMessage("Grade já selecionada!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listagrade.remove((listagrade.size() - 1));
			return;
		}
		//tratamento para nao deixar selecionar o mesmo aluno novamente
		if (listaAlunosSelecionados != null && listaAlunosSelecionados.size() > 0 &&  listaAlunosSelecionados.indexOf(listaAlunosSelecionados.get((listaAlunosSelecionados.size() - 1 ))) != (listaAlunosSelecionados.size() - 1) && listaAlunosSelecionados.size() != 1){
			FacesMessage msg = new FacesMessage("Aluno já selecionado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaAlunosSelecionados.remove((listaAlunosSelecionados.size() - 1));
			return;
		}
		//tratamento para nao deixar selecionar o mesmo ano novamente
		if (listaAnosSelecionados != null && listaAnosSelecionados.size() > 0 && listaAnosSelecionados.indexOf(listaAnosSelecionados.get((listaAnosSelecionados.size() - 1 ))) != (listaAnosSelecionados.size() - 1) && listaAnosSelecionados.size() != 1){
			FacesMessage msg = new FacesMessage("Ano já selecionado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaAnosSelecionados.remove((listaAnosSelecionados.size() - 1));
			return;
		}
		//tratamento para nao deixar selecionar o mesmo ano invertido novamente
		if (listaAnosSelecionadosInvertida != null && listaAnosSelecionadosInvertida.size() > 0 && listaAnosSelecionadosInvertida.indexOf(listaAnosSelecionadosInvertida.get((listaAnosSelecionadosInvertida.size() - 1 ))) != (listaAnosSelecionadosInvertida.size() - 1) && listaAnosSelecionadosInvertida.size() != 1){
			FacesMessage msg = new FacesMessage("Ano já selecionado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaAnosSelecionadosInvertida.remove((listaAnosSelecionadosInvertida.size() - 1));
			return;
		}
		//reseta tabelas
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:discplinas");
		dataTable.clearInitialState();
		dataTable.reset();
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:estimativas");
		dataTable.clearInitialState();
		dataTable.reset();
		//verificar se existe ou tem somente um grade selecionada
		if ((listagrade == null || listagrade.size() == 0 || listagrade.size() == 1) && listaTodasDisciplinasSelecionadas.size() == 0){
			lgPeriodoUm = false;
			lgPeriodoCinco = false;
			lgPeriodoNove = false;
			lgEletivas = false;
		}
		else {
			lgPeriodoUm = true;
			lgPeriodoCinco = true;
			lgPeriodoNove = true;
			lgEletivas = true;
		}

		if (i==0){ //se inseriu uma nova grade ou retirou limpa o campo de disciplinas selecionadas
			listaTodasDisciplinasSelecionadas = new ArrayList<String>();
			listaAlunosSelecionados = new ArrayList<String>();
			listaAnosSelecionadosInvertida = new ArrayList<String>();
			listaAnosSelecionados = new ArrayList<String>();
		}

		if(i==1 || i==2){
			listaAlunosSelecionados = new ArrayList<String>();
		} 


		if (i==5){
			listaAnosSelecionadosInvertida = new ArrayList<String>();
			listaAnosSelecionados = new ArrayList<String>();
		}
		gerarDados();	
		FacesMessage msg = new FacesMessage("Filtro Aplicado!");
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public void onItemSelectEstimativa() {
		if ((selectedOptionsEstimativa.contains("E") && selectedOptionsEstimativa.contains("B")) || (!selectedOptionsEstimativa.contains("E") && !selectedOptionsEstimativa.contains("B"))){
			lgBarra = true;
			lgEstimativa = true;
			lgPlanejado = true;

		}
		else if (selectedOptionsEstimativa.contains("E") && !selectedOptionsEstimativa.contains("B")){
			lgEstimativa = true;
			lgPlanejado = false;
			lgBarra = false;

		}
		else if (!selectedOptionsEstimativa.contains("E") && selectedOptionsEstimativa.contains("B")){
			lgEstimativa = false;
			lgPlanejado = true;
			lgBarra = false;
		}
	}

	public void onItemSelect() {
		if (listaTodasDisciplinasSelecionadas != null && listaTodasDisciplinasSelecionadas.size() > 0 && listaTodasDisciplinasSelecionadas.indexOf(listaTodasDisciplinasSelecionadas.get((listaTodasDisciplinasSelecionadas.size() - 1 ))) != (listaTodasDisciplinasSelecionadas.size() - 1) && listaTodasDisciplinasSelecionadas.size() != 1){
			FacesMessage msg = new FacesMessage("Disciplina já selecionada!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaTodasDisciplinasSelecionadas.remove((listaTodasDisciplinasSelecionadas.size() - 1));
			return;
		}

		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:discplinas");
		dataTable.clearInitialState();
		dataTable.reset();
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:estimativas");
		dataTable.clearInitialState();
		dataTable.reset();

		if ((listaTodasDisciplinasSelecionadas == null || listaTodasDisciplinasSelecionadas.size() == 0) && listagrade.size() == 1){
			lgPeriodoUm = false;
			lgPeriodoCinco = false;
			lgPeriodoNove = false;
			lgEletivas = false;
		}
		else{
			lgPeriodoUm = true;
			lgPeriodoCinco = true;
			lgPeriodoNove = true;
			lgEletivas = true;
		}

		gerarDados();
		FacesMessage msg = new FacesMessage("Filtro Aplicado!");
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	//========================================================= CLASSE ==================================================================================//

	static public class ColumnModel implements Serializable {  

		private static final long serialVersionUID = 1L;

		private String header;  
		private int mes;  
		private String cor;


		public ColumnModel(String header, int mes,String cor) {  
			this.header = header;  
			this.mes = mes;
			this.cor = cor;

		}  

		public String getHeader() {  
			return header;  
		}  

		public String getCor() {  
			return "background-color:" + cor + ";";  
		}  

		public String retornaProperty(GradeHistorico c) {  

			return c.getHistoricoAluno().get(mes);
		}  
	}

	//========================================================= GET - SET ==================================================================================//

	public int getI() {
		return i;
	}


	public void setI(int i) {
		this.i = i;
	}


	public ImportarArvore getImportador() {
		return importador;
	}


	public void setImportador(ImportarArvore importador) {
		this.importador = importador;
	}


	public Curriculum getCurriculum() {
		return curriculum;
	}


	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}


	public Aluno getAluno() {
		return aluno;
	}


	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<String> getSelectedOptionsPeriodo() {
		return selectedOptionsPeriodo;
	}


	public void setSelectedOptionsPeriodo(List<String> selectedOptionsPeriodo) {
		this.selectedOptionsPeriodo = selectedOptionsPeriodo;
	}


	public List<String> getSelectedOptionsPeriodoDois() {
		return selectedOptionsPeriodoDois;
	}


	public void setSelectedOptionsPeriodoDois(
			List<String> selectedOptionsPeriodoDois) {
		this.selectedOptionsPeriodoDois = selectedOptionsPeriodoDois;
	}


	public List<String> getSelectedOptionsPeriodoTres() {
		return selectedOptionsPeriodoTres;
	}


	public void setSelectedOptionsPeriodoTres(
			List<String> selectedOptionsPeriodoTres) {
		this.selectedOptionsPeriodoTres = selectedOptionsPeriodoTres;
	}


	public List<String> getSelectedOptionsSituacao() {
		return selectedOptionsSituacao;
	}


	public void setSelectedOptionsSituacao(List<String> selectedOptionsSituacao) {
		this.selectedOptionsSituacao = selectedOptionsSituacao;
	}


	public List<String> getSelectedOptionsSituacaoDois() {
		return selectedOptionsSituacaoDois;
	}


	public void setSelectedOptionsSituacaoDois(
			List<String> selectedOptionsSituacaoDois) {
		this.selectedOptionsSituacaoDois = selectedOptionsSituacaoDois;
	}


	public List<String> getSelectedOptionsSituacaoTres() {
		return selectedOptionsSituacaoTres;
	}


	public void setSelectedOptionsSituacaoTres(
			List<String> selectedOptionsSituacaoTres) {
		this.selectedOptionsSituacaoTres = selectedOptionsSituacaoTres;
	}


	public ArrayList<String> getLista() {
		return lista;
	}


	public void setLista(ArrayList<String> lista) {
		this.lista = lista;
	}






	public List<ColumnModel> getColumns() {
		return columns;
	}


	public void setColumns(List<ColumnModel> columns) {
		this.columns = columns;
	}


	public List<GradeHistorico> getListaGradeHistorico() {
		return listaGradeHistorico;
	}


	public void setListaGradeHistorico(List<GradeHistorico> listaGradeHistorico) {
		this.listaGradeHistorico = listaGradeHistorico;
	}


	public List<Aluno> getListaAluno() {
		return listaAluno;
	}


	public void setListaAluno(List<Aluno> listaAluno) {
		this.listaAluno = listaAluno;
	}


	public List<Historico> getListaHistorico() {
		return listaHistorico;
	}


	public void setListaHistorico(List<Historico> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}


	public List<GradeDisciplina> getListagradeDisciplina() {
		return listagradeDisciplina;
	}


	public void setListagradeDisciplina(List<GradeDisciplina> listagradeDisciplina) {
		this.listagradeDisciplina = listagradeDisciplina;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public ArrayList<String> getListaDisciplinasDisponiveis() {
		return listaDisciplinasDisponiveis;
	}


	public void setListaDisciplinasDisponiveis(
			ArrayList<String> listaDisciplinasDisponiveis) {
		this.listaDisciplinasDisponiveis = listaDisciplinasDisponiveis;
	}


	public List<String> getSelectedOptionsObrigatoria() {
		return selectedOptionsObrigatoria;
	}


	public void setSelectedOptionsObrigatoria(
			List<String> selectedOptionsObrigatoria) {
		this.selectedOptionsObrigatoria = selectedOptionsObrigatoria;
	}


	public List<String> getSelectedOptionsEletiva() {
		return selectedOptionsEletiva;
	}


	public void setSelectedOptionsEletiva(List<String> selectedOptionsEletiva) {
		this.selectedOptionsEletiva = selectedOptionsEletiva;
	}


	public List<String> getSelectedOptionsOpcional() {
		return selectedOptionsOpcional;
	}


	public void setSelectedOptionsOpcional(List<String> selectedOptionsOpcional) {
		this.selectedOptionsOpcional = selectedOptionsOpcional;
	}


	public List<String> getSelectedOptionsSituacaoQuatro() {
		return selectedOptionsSituacaoQuatro;
	}


	public void setSelectedOptionsSituacaoQuatro(
			List<String> selectedOptionsSituacaoQuatro) {
		this.selectedOptionsSituacaoQuatro = selectedOptionsSituacaoQuatro;
	}


	public Curso getCurso() {
		return curso;
	}


	public void setCurso(Curso curso) {
		this.curso = curso;
	}


	public UsuarioController getUsuarioController() {
		return usuarioController;
	}


	public void setUsuarioController(UsuarioController usuarioController) {
		this.usuarioController = usuarioController;
	}

	public List<String> getListagrade() {
		return listagrade;
	}


	public void setListagrade(List<String> listagrade) {
		this.listagrade = listagrade;
	}


	public ArrayList<String> getListaTodasDisciplinasDisponiveis() {
		return listaTodasDisciplinasDisponiveis;
	}


	public void setListaTodasDisciplinasDisponiveis(
			ArrayList<String> listaTodasDisciplinasDisponiveis) {
		this.listaTodasDisciplinasDisponiveis = listaTodasDisciplinasDisponiveis;
	}


	public ArrayList<Class> getClasses() {
		return classes;
	}


	public void setClasses(ArrayList<Class> classes) {
		this.classes = classes;
	}


	public Grade getGrade() {
		return grade;
	}


	public void setGrade(Grade grade) {
		this.grade = grade;
	}


	public EstruturaArvore getEstruturaArvore() {
		return estruturaArvore;
	}


	public void setEstruturaArvore(EstruturaArvore estruturaArvore) {
		this.estruturaArvore = estruturaArvore;
	}


	public ArrayList<String> getListaTodasDisciplinasSelecionadas() {
		return listaTodasDisciplinasSelecionadas;
	}


	public void setListaTodasDisciplinasSelecionadas(
			ArrayList<String> listaTodasDisciplinasSelecionadas) {
		this.listaTodasDisciplinasSelecionadas = listaTodasDisciplinasSelecionadas;
	}


	public boolean isLgPeriodoUm() {
		return lgPeriodoUm;
	}


	public void setLgPeriodoUm(boolean lgPeriodoUm) {
		this.lgPeriodoUm = lgPeriodoUm;
	}


	public boolean isLgPeriodoCinco() {
		return lgPeriodoCinco;
	}


	public void setLgPeriodoCinco(boolean lgPeriodoCinco) {
		this.lgPeriodoCinco = lgPeriodoCinco;
	}


	public boolean isLgPeriodoNove() {
		return lgPeriodoNove;
	}


	public void setLgPeriodoNove(boolean lgPeriodoNove) {
		this.lgPeriodoNove = lgPeriodoNove;
	}


	public boolean isLgEletivas() {
		return lgEletivas;
	}


	public void setLgEletivas(boolean lgEletivas) {
		this.lgEletivas = lgEletivas;
	}


	public ArrayList<String> getListaAlunosSelecionados() {
		return listaAlunosSelecionados;
	}


	public void setListaAlunosSelecionados(ArrayList<String> listaAlunosSelecionados) {
		this.listaAlunosSelecionados = listaAlunosSelecionados;
	}


	public ArrayList<String> getListaAnosSelecionados() {
		return listaAnosSelecionados;
	}


	public void setListaAnosSelecionados(ArrayList<String> listaAnosSelecionados) {
		this.listaAnosSelecionados = listaAnosSelecionados;
	}


	public ArrayList<String> getListaAlunosTodos() {
		return listaAlunosTodos;
	}


	public void setListaAlunosTodos(ArrayList<String> listaAlunosTodos) {
		this.listaAlunosTodos = listaAlunosTodos;
	}


	public ArrayList<String> getListaAnoTodos() {
		return listaAnoTodos;
	}


	public void setListaAnoTodos(ArrayList<String> listaAnoTodos) {
		this.listaAnoTodos = listaAnoTodos;
	}


	public List<EspectativaDisciplina> getListaEspectativaDisciplina() {
		return listaEspectativaDisciplina;
	}


	public void setListaEspectativaDisciplina(
			List<EspectativaDisciplina> listaEspectativaDisciplina) {
		this.listaEspectativaDisciplina = listaEspectativaDisciplina;
	}


	public int getPercNrMatriculados() {
		return percNrMatriculados;
	}


	public void setPercNrMatriculados(int percNrMatriculados) {
		this.percNrMatriculados = percNrMatriculados;
	}


	public int getPercNrRN() {
		return percNrRN;
	}


	public void setPercNrRN(int percNrRN) {
		this.percNrRN = percNrRN;
	}


	public int getPercNrRF() {
		return percNrRF;
	}


	public void setPercNrRF(int percNrRF) {
		this.percNrRF = percNrRF;
	}


	public int getPercNrH() {
		return percNrH;
	}


	public void setPercNrH(int percNrH) {
		this.percNrH = percNrH;
	}


	public int getPercNrQH() {
		return percNrQH;
	}


	public void setPercNrQH(int percNrQH) {
		this.percNrQH = percNrQH;
	}


	public List<EspectativaDisciplina> getListaEspectativaDisciplinaSelecionada() {
		return listaEspectativaDisciplinaSelecionada;
	}


	public void setListaEspectativaDisciplinaSelecionada(
			List<EspectativaDisciplina> listaEspectativaDisciplinaSelecionada) {
		this.listaEspectativaDisciplinaSelecionada = listaEspectativaDisciplinaSelecionada;
	}


	public List<GradeHistorico> getListaGradeHistoricoSelecionadas() {
		return listaGradeHistoricoSelecionadas;
	}


	public void setListaGradeHistoricoSelecionadas(
			List<GradeHistorico> listaGradeHistoricoSelecionadas) {
		this.listaGradeHistoricoSelecionadas = listaGradeHistoricoSelecionadas;
	}


	public boolean isLdGridHistorico() {
		return ldGridHistorico;
	}


	public void setLdGridHistorico(boolean ldGridHistorico) {
		this.ldGridHistorico = ldGridHistorico;
	}


	public GradeHistorico getSelecionadoGradeHistorico() {
		return selecionadoGradeHistorico;
	}


	public void setSelecionadoGradeHistorico(
			GradeHistorico selecionadoGradeHistorico) {
		this.selecionadoGradeHistorico = selecionadoGradeHistorico;
	}


	public List<String> getSelectedOptionsEstimativa() {
		return selectedOptionsEstimativa;
	}


	public void setSelectedOptionsEstimativa(List<String> selectedOptionsEstimativa) {
		this.selectedOptionsEstimativa = selectedOptionsEstimativa;
	}


	public boolean isLgEstimativa() {
		return lgEstimativa;
	}


	public void setLgEstimativa(boolean lgEstimativa) {
		this.lgEstimativa = lgEstimativa;
	}


	public boolean isLgPlanejado() {
		return lgPlanejado;
	}


	public void setLgPlanejado(boolean lgPlanejado) {
		this.lgPlanejado = lgPlanejado;
	}


	public boolean isLgBarra() {
		return lgBarra;
	}


	public void setLgBarra(boolean lgBarra) {
		this.lgBarra = lgBarra;
	}


	public GradeHistorico getGradeHistoricoSelecionado() {
		return gradeHistoricoSelecionado;
	}


	public void setGradeHistoricoSelecionado(
			GradeHistorico gradeHistoricoSelecionado) {
		this.gradeHistoricoSelecionado = gradeHistoricoSelecionado;
	}


	public ArrayList<String> getListaAnosSelecionadosInvertida() {
		return listaAnosSelecionadosInvertida;
	}


	public void setListaAnosSelecionadosInvertida(
			ArrayList<String> listaAnosSelecionadosInvertida) {
		this.listaAnosSelecionadosInvertida = listaAnosSelecionadosInvertida;
	}
}
