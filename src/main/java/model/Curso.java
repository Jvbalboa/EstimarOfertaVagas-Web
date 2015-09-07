package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
//@Table(name="postgres.V_SSF_COLABORADOR_LOTACAO")
public class Curso {
	// ==========================VARIÃ�VEIS=================================================================================================================//
		
		private String codigo;
		private String nomeCurso ;
		
		


		// ==========================MÃ‰TODOS===================================================================================================================//
		public Curso() {
			super();
		}

		// ==========================GETTERS_AND_SETTERS======================================================================================================//

		@Column (name="CODIGO")
		@Id
		public String getCodigo() {
			return codigo;
		}

		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}

		@Column(name="NOME_CURSO")
		public String getNomeCurso() {
			return nomeCurso;
		}

		public void setNomeCurso(String nomeCurso) {
			this.nomeCurso = nomeCurso;
		}

		



}

