package br.ufjf.coordenacao.sistemagestaocurso.util.arvore;

import java.util.List;
import java.util.TreeSet;

import br.ufjf.coordenacao.OfertaVagas.model.*;
import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.sistemagestaocurso.model.*;



public class ImportarArvore {

	private Grade grade;
	private EstruturaArvore estruturaArvore;
	private boolean lgProcessadoGrade = false;
	private boolean lgProcessadoHistorico = false;	
	private StudentsHistory sh = new StudentsHistory();
	private Curriculum _cur = new Curriculum();
	private Boolean consideraCoGrade;
	private Boolean resetarStance = false;
	
	
	public void importarHistorico(List<Historico> listaHistorico){
		for (Historico historico:listaHistorico){
			ClassStatus status;
			String classStatus = historico.getStatusDisciplina();
			if (classStatus.equals("Matriculado")) status = ClassStatus.ENROLLED;
			else if (classStatus.equals("Aprovado") || classStatus.equals("Dispensado")) status = ClassStatus.APPROVED;
			else if (classStatus.equals("Rep Nota")) status = ClassStatus.REPROVED_GRADE;
			else if (classStatus.equals("Rep Freq")) status = ClassStatus.REPROVED_FREQUENCY;
			else if (classStatus.equals("Trancado")) status = ClassStatus.NOT_ENROLLED;
			else continue; 

			sh.add(
					historico.getAluno().getMatricula(),                      //matricula aluno
					historico.getAluno().getNome(),                           //nome aluno
					historico.getAluno().getCurso().getCodigo(),			  //codigo curso
					historico.getAluno().getGrade().getCodigo(),              //codigo grade
					historico.getSemestreCursado(),                           //semestre cursado
					historico.getDisciplina().getCodigo(),                    //codigo disciplina
					status,                                                   //status disciplina
					historico.getNota(),                                      //nota disciplina
					historico.getDisciplina().getCargaHoraria().toString()   //carga hor�ria disciplina					
					);                                                  
		}		
	}
	
	

	public void importarDisciplinas(Grade grade,boolean consideraCo){
		
		//ja esta no estado que eu preciso
		if (consideraCoGrade != null && consideraCo == consideraCoGrade){
			return;
		}
		
		

		estruturaArvore = EstruturaArvore.getInstance();
		
		
		
		_cur = new Curriculum();
		List<GradeDisciplina> listaGradeDisciplina = grade.getGrupoGradeDisciplina();		
		for(GradeDisciplina gradeDisciplina : listaGradeDisciplina){
			List<PreRequisito> listaPreRequisito = gradeDisciplina.getPreRequisito();
			//subindo as obrigatorias e seus requisitos
			if (gradeDisciplina.getTipoDisciplina().equals("Obrigatoria") ){
				String semester = String.valueOf(gradeDisciplina.getPeriodo());
				String _class = gradeDisciplina.getDisciplina().getCodigo();
				Class c = ClassFactory.getClass(grade.getCurso().getCodigo(),grade.getCodigo(),_class);
				c.getPrerequisite().clear();
				c.getCorequisite().clear();
				_cur.addMandatoryClass(Integer.valueOf(semester), c);
				for (PreRequisito prerequisito:listaPreRequisito){
					String prerequisite = prerequisito.getDisciplina().getCodigo(); 
					Class pre = ClassFactory.getClass(grade.getCurso().getCodigo(),grade.getCodigo(),prerequisite);
					if (prerequisito.getTipo().equals("Co-Requisito")){
						c.addCorequisite(pre);
					}
					else{
						c.addPrerequisite(pre);
					}
				}
				c.setWorkload(Integer.valueOf(gradeDisciplina.getDisciplina().getCargaHoraria()));
			}
			else {
				//subindo eletivas e seus requisitos
				Class c = ClassFactory.getClass(grade.getCurso().getCodigo(),grade.getCodigo(),gradeDisciplina.getDisciplina().getCodigo());
				_cur.addElectiveClass(c);
				for (PreRequisito prerequisito:listaPreRequisito){
					String prerequisite = prerequisito.getDisciplina().getCodigo(); 
					Class pre = ClassFactory.getClass(grade.getCurso().getCodigo(),grade.getCodigo(),prerequisite);
					c.addPrerequisite(pre);
				}
				c.setWorkload(Integer.valueOf(gradeDisciplina.getDisciplina().getCargaHoraria()));
			}
		}		
		List<Equivalencia> listaEquivalencia = grade.getGrupoEquivalencia();
		//subindo as equivalencias
		for(Equivalencia equivalencia : listaEquivalencia){
			
			String idDaGrade = equivalencia.getDisciplinaGrade().getCodigo();
			String idNaoDaGrade = equivalencia.getDisciplinaEquivalente().getCodigo();

			Class c = null;
			if (!ClassFactory.contains(grade.getCurso().getCodigo(),grade.getCodigo(), idDaGrade)
					&& ClassFactory.contains(grade.getCurso().getCodigo(),grade.getCodigo(), idNaoDaGrade)) {
				String aux = idNaoDaGrade;
				idNaoDaGrade = idDaGrade;
				idDaGrade = aux;
			}

			c = ClassFactory.getClass(grade.getCurso().getCodigo(),grade.getCodigo(), idDaGrade);
			ClassFactory.getClass(grade.getCurso().getCodigo(),grade.getCodigo(), idNaoDaGrade);
			ClassFactory.addClass(grade.getCurso().getCodigo(),grade.getCodigo(), idNaoDaGrade, c);			
			
		}

		if (consideraCo == true){
			considerarCo();
		}
		
		TreeSet<String> _IRA_skipClasses = new TreeSet<String>();
		
		for(GradeDisciplina gradeDisciplinaSelecionada : grade.getGrupoGradeDisciplina()){
			if(gradeDisciplinaSelecionada.getExcluirIra() == true){		
				_IRA_skipClasses.add(gradeDisciplinaSelecionada.getDisciplina().getCodigo());
			}
		}
		_cur.setIRA_skipClasses(_IRA_skipClasses);
		CurriculumFactory.putCurriculum(grade.getCurso().getCodigo(),grade.getCodigo(), _cur);
		
		consideraCoGrade = consideraCo;

	}

	public void considerarCo(){

		/*for(int i: _cur.getMandatories().keySet())
		{
			Iterator<Class> it = _cur.getMandatories().get(i).iterator();

			ArrayList<Class> duplicated = new ArrayList<Class>();
			ArrayList<Class> insert = new ArrayList<Class>();
			while(it.hasNext())
			{			
				Class c = it.next();
				if(!c.getCorequisite().isEmpty())
				{
					ClassContainer cc = new ClassContainer("Co-requisitos");
					cc.addClass(c);

					for(Class cr: c.getCorequisite())
					{
						duplicated.add(cr);
						cc.addClass(cr);
					}
					it.remove();
					insert.add(cc);
				}
			}

			_cur.getMandatories().get(i).removeAll(duplicated);
			_cur.getMandatories().get(i).addAll(insert);
		}*/
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public boolean isLgProcessadoHistorico() {
		return lgProcessadoHistorico;
	}

	public void setLgProcessadoHistorico(boolean lgProcessadoHistorico) {
		this.lgProcessadoHistorico = lgProcessadoHistorico;
	}

	public boolean isLgProcessadoGrade() {
		return lgProcessadoGrade;
	}

	public void setLgProcessadoGrade(boolean lgProcessadoGrade) {
		this.lgProcessadoGrade = lgProcessadoGrade;
	}

	public StudentsHistory getSh() {
		return sh;
	}

	public void setSh(StudentsHistory sh) {
		this.sh = sh;
	}

	public Curriculum get_cur() {
		return _cur;
	}

	public void set_cur(Curriculum _cur) {
		this._cur = _cur;
	}

	public EstruturaArvore getEstruturaArvore() {
		return estruturaArvore;
	}

	public void setEstruturaArvore(EstruturaArvore estruturaArvore) {
		this.estruturaArvore = estruturaArvore;
	}

	public Boolean getConsideraCoGrade() {
		return consideraCoGrade;
	}

	public void setConsideraCoGrade(Boolean consideraCoGrade) {
		this.consideraCoGrade = consideraCoGrade;
	}



	public Boolean getResetarStance() {
		return resetarStance;
	}



	public void setResetarStance(Boolean resetarStance) {
		this.resetarStance = resetarStance;
	}



	
	
}
