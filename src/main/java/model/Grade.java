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
public class Grade {
	// ==========================VARIÃ�VEIS=================================================================================================================//
		
		private String cod;
		private String cursoCodigo;
		
		


		// ==========================MÃ‰TODOS===================================================================================================================//
		public Grade() {
			super();
		}

		// ==========================GETTERS_AND_SETTERS======================================================================================================//

		@Column (name="COD")
		@Id
		public String getCod() {
			return cod;
		}

		public void setCod(String cod) {
			this.cod = cod;
		}

		@Column(name="CURSO_CODIGO")
		public String getCursoCodigo() {
			return cursoCodigo;
		}

		public void setCursoCodigo(String cursoCodigo) {
			this.cursoCodigo = cursoCodigo;
		}

		

}

