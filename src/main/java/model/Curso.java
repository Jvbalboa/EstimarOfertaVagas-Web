package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;




@Entity
@SequenceGenerator(name="curso_sequencia", sequenceName="curso_seq", allocationSize=1)  
public class Curso {
	
	// ==========================VARIÃ�VEIS=================================================================================================================//
		
		private String codigo;
		private String nome ;
		private List<Aluno> grupoAlunos = new ArrayList<Aluno>();
		private List<Grade> grupoGrades = new ArrayList<Grade>();
		private Long id;
		private List<PessoaCurso> pessoaCurso = new ArrayList<PessoaCurso>();
		private String dataAtualizacao;
		
		// ==========================GETTERS_AND_SETTERS======================================================================================================//

		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY, generator="curso_sequencia")
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
	
		@Column (name="CODIGO", unique = true, nullable = false)
		public String getCodigo() {
			return codigo;
		}

		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		
		@Column(name="NOME"  )
		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		@OneToMany(mappedBy = "curso", targetEntity = Aluno.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
		public List<Aluno> getGrupoAlunos() {
			return grupoAlunos;
		}

		public void setGrupoAlunos(List<Aluno> grupoAlunos) {
			this.grupoAlunos = grupoAlunos;
		}

		@OneToMany(mappedBy = "curso", targetEntity = Grade.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
		public List<Grade> getGrupoGrades() {
			return grupoGrades;
		}

		public void setGrupoGrades(List<Grade> grupoGrades) {
			this.grupoGrades = grupoGrades;
		}
		
		@OneToMany(mappedBy = "curso", targetEntity = PessoaCurso.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
		public List<PessoaCurso> getPessoaCurso() {
			return pessoaCurso;
		}
		public void setPessoaCurso(List<PessoaCurso> pessoaCurso) {
			this.pessoaCurso = pessoaCurso;
		}

		@Column (name="DT_ATUALIZACAO", nullable = true )
		public String getDataAtualizacao() {
			return dataAtualizacao;
		}

		public void setDataAtualizacao(String dataAtualizacao) {
			this.dataAtualizacao = dataAtualizacao;
		}		
}