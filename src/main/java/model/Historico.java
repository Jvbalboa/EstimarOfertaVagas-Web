package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
//@Table(name="postgres.V_SSF_COLABORADOR_LOTACAO")
public class Historico {
	// ==========================VARIÃ�VEIS=================================================================================================================//
		
		private String alunoMatricula;
		private String disciplinaCod;
		private String semestreCursado;
		
		private String statusDisciplina ;
		


		// ==========================MÃ‰TODOS===================================================================================================================//
		public Historico() {
			super();
		}

		// ==========================GETTERS_AND_SETTERS======================================================================================================//

		@Column (name="ALUNO_MATRICULA")
		@Id
		public String getAlunoMatricula() {
			return alunoMatricula;
		}

		public void setAlunoMatricula(String alunoMatricula) {
			this.alunoMatricula = alunoMatricula;
		}

		@Column(name="DISCIPLINA_COD")
		public String getDisciplinaCod() {
			return disciplinaCod;
		}

		public void setDisciplinaCod(String disciplinaCod) {
			this.disciplinaCod = disciplinaCod;
		}
		@Column(name="SEMESTRE_CURSADO")
		public String getSemestreCursado() {
			return semestreCursado;
		}

		public void setSemestreCursado(String semestreCursado) {
			this.semestreCursado = semestreCursado;
		}
		@Column(name="STATUS_DISCIPLINA")
		public String getStatusDisciplina() {
			return statusDisciplina;
		}

		public void setStatusDisciplina(String statusDisciplina) {
			this.statusDisciplina = statusDisciplina;
		}


		
		
		
		
		
		

}

