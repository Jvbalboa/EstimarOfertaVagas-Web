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
public class Disciplina {
	// ==========================VARIÃ�VEIS=================================================================================================================//
		
		private String cod;
		private String nome;
		
		


		// ==========================MÃ‰TODOS===================================================================================================================//
		public Disciplina() {
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

		@Column(name="NOME")
		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		




}

