package br.ufjf.coordenacao.sistemagestaocurso.model.estrutura;

public class DisciplinaPlanejamento {
	
	private String codigo;
	private Integer cargaHoraria;
	private String cor;	
	private String andou ;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public Integer getCargaHoraria() {
		return cargaHoraria;
	}
	
	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
	
	public String getCor() {
		return cor;
	}
	
	public void setCor(String cor) {
		this.cor = cor;
	}
	public String getAndou() {
		return andou;
	}
	public void setAndou(String andou) {
		this.andou = andou;
	}	
}