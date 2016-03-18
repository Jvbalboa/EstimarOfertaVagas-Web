package model.estrutura;


public class SituacaoDisciplina {	
	
	private String codigo;
	private String nome;
	private String situacao;
	private String cargaHoraria;
	private String periodo;
	private String listaPreRequisitos;
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getSituacao() {
		return situacao;
	}
	
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	public String getCargaHoraria() {
		return cargaHoraria;
	}
	
	public void setCargaHoraria(String cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
	
	public String getPeriodo() {
		return periodo;
	}
	
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	public String getListaPreRequisitos() {
		return listaPreRequisitos;
	}
	
	public void setListaPreRequisitos(String listaPreRequisitos) {
		this.listaPreRequisitos = listaPreRequisitos;
	}	
}