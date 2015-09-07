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
public class GradeDisciplina {
	// ==========================VARIÃ�VEIS=================================================================================================================//
		
		private String gradeCod;
		private String disciplinaCod;
		private String tipoDisciplina;
		private Long periodo;
		


		// ==========================MÃ‰TODOS===================================================================================================================//
		public GradeDisciplina() {
			super();
		}

		// ==========================GETTERS_AND_SETTERS======================================================================================================//

		@Column (name="GRADE_COD")
		@Id
		public String getGradeCod() {
			return gradeCod;
		}

		public void setGradeCod(String gradeCod) {
			this.gradeCod = gradeCod;
		}

		@Column(name="DISCIPLINA_COD")
		public String getDisciplinaCod() {
			return disciplinaCod;
		}

		public void setDisciplinaCod(String disciplinaCod) {
			this.disciplinaCod = disciplinaCod;
		}
		@Column(name="TIPO_DISCIPLINA")
		public String getTipoDisciplina() {
			return tipoDisciplina;
		}

		public void setTipoDisciplina(String tipoDisciplina) {
			this.tipoDisciplina = tipoDisciplina;
		}
		@Column(name="PERIODO")
		public Long getPeriodo() {
			return periodo;
		}

		public void setPeriodo(Long periodo) {
			this.periodo = periodo;
		}


		
		
		
		
		
		

}

