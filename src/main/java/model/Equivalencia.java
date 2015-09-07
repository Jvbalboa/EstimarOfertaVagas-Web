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
public class Equivalencia {
	// ==========================VARIÃ�VEIS=================================================================================================================//
		
		private String disciplinaDestino;
		private String disciplinaCod;
		private String gradeCod ;
		


		// ==========================MÃ‰TODOS===================================================================================================================//
		public Equivalencia() {
			super();
		}

		// ==========================GETTERS_AND_SETTERS======================================================================================================//

		@Column (name="DESCIPLINA_DESTINO")
		@Id
		public String getDisciplinaDestino() {
			return disciplinaDestino;
		}

		public void setDisciplinaDestino(String disciplinaDestino) {
			this.disciplinaDestino = disciplinaDestino;
		}

		@Column(name="DESCIPLINA_COD")
		public String getDisciplinaCod() {
			return disciplinaCod;
		}

		public void setDisciplinaCod(String disciplinaCod) {
			this.disciplinaCod = disciplinaCod;
		}

		@Column(name="GRADE_COD")
		public String getGradeCod() {
			return gradeCod;
		}

		public void setGradeCod(String gradeCod) {
			this.gradeCod = gradeCod;
		}

}

