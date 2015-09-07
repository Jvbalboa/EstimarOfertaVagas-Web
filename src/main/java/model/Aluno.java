package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
//@Table(name="postgres.V_SSF_COLABORADOR_LOTACAO")
public class Aluno {
	// ==========================VARIÃ�VEIS=================================================================================================================//
		
		private String matricula;
		private String gradeCod;
		private String cursoCodigo;
		private String nome;
		private String periodoIngresso;
		private String teste;
		
	
		


		
		

		// ==========================MÃ‰TODOS===================================================================================================================//
		public Aluno() {
			super();
		}

		// ==========================GETTERS_AND_SETTERS======================================================================================================//

		@Column (name="MATRICULA")
		@Id
		public String getMatricula() {
			return matricula;
		}

		public void setMatricula(String matricula) {
			this.matricula = matricula;
		}

		@Column(name="NOME")
		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		@Column(name="GRADE_COD")
		public String getGradeCod() {
			return gradeCod;
		}

		public void setGradeCod(String gradeCod) {
			this.gradeCod = gradeCod;
		}

		@Column(name="CURSO_CODIGO")
		public String getCursoCodigo() {
			return cursoCodigo;
		}

		public void setCursoCodigo(String cursoCodigo) {
			this.cursoCodigo = cursoCodigo;
		}

		@Column(name="PERIODO_INGRESSO")
		public String getPeriodoIngresso() {
			return periodoIngresso;
		}

		public void setPeriodoIngresso(String periodoIngresso) {
			this.periodoIngresso = periodoIngresso;
		}




}

