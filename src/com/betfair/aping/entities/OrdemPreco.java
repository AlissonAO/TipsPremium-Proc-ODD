package com.betfair.aping.entities;

public class OrdemPreco {

	private Double preco;
	private Double size;
	private Long idgalgo;
	private String idMercado;
	private String nome;
	private Boolean lanceAbortado = Boolean.FALSE;
	private String numGalgo;
	
	
	
	
	public String getNumGalgo() {
		return numGalgo;
	}
	public void setNumGalgo(String numGalgo) {
		this.numGalgo = numGalgo;
	}
	public Boolean getLanceAbortado() {
		return lanceAbortado;
	}
	public void setLanceAbortado(Boolean lanceAbortado) {
		this.lanceAbortado = lanceAbortado;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getIdMercado() {
		return idMercado;
	}
	public void setIdMercado(String idMercado) {
		this.idMercado = idMercado;
	}
	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
	}
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	public Long getIdgalgo() {
		return idgalgo;
	}
	public void setIdgalgo(Long idgalgo) {
		this.idgalgo = idgalgo;
	}
	
	
	
}
