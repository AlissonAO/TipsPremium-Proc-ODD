package com.betfair.aping.entities;

public class RetornoOrdemVO {

	
	private Boolean stautsLance;
	private Double valorRed;
	private Integer qtdRed; 
	private String nomeCorrida;
	private String NomeGalgo;
	private String numGalgo;
	private String valorFechamento;
	private String horaCorrida;
	private Boolean deuRed = Boolean.FALSE;
	private Boolean abortaLance = Boolean.FALSE;
	private Boolean vemdeREd = Boolean.FALSE;
	private Double valorTotalRed = 0.0;
	
	
	

	
	public Double getValorTotalRed() {
		return valorTotalRed;
	}

	public void setValorTotalRed(Double valorTotalRed) {
		this.valorTotalRed = valorTotalRed;
	}

	public String getHoraCorrida() {
		return horaCorrida;
	}

	public void setHoraCorrida(String horaCorrida) {
		this.horaCorrida = horaCorrida;
	}

	public String getNomeCorrida() {
		return nomeCorrida;
	}

	public Boolean getVemdeREd() {
		return vemdeREd;
	}

	public void setVemdeREd(Boolean vemdeREd) {
		this.vemdeREd = vemdeREd;
	}

	public void setNomeCorrida(String nomeCorrida) {
		this.nomeCorrida = nomeCorrida;
	}

	public String getNomeGalgo() {
		return NomeGalgo;
	}

	public void setNomeGalgo(String nomeGalgo) {
		NomeGalgo = nomeGalgo;
	}

	public String getNumGalgo() {
		return numGalgo;
	}

	public void setNumGalgo(String numGalgo) {
		this.numGalgo = numGalgo;
	}

	public String getValorFechamento() {
		return valorFechamento;
	}

	public void setValorFechamento(String valorFechamento) {
		this.valorFechamento = valorFechamento;
	}

	public Boolean getAbortaLance() {
		return abortaLance;
	}

	public void setAbortaLance(Boolean abortaLance) {
		this.abortaLance = abortaLance;
	}

	public Integer getQtdRed() {
		return qtdRed;
	}

	public void setQtdRed(Integer qtdRed) {
		this.qtdRed = qtdRed;
	}

	public Double getValorRed() {
		return valorRed;
	}

	public void setValorRed(Double valorRed) {
		this.valorRed = valorRed;
	}

	public Boolean getDeuRed() {
		return deuRed;
	}

	public void setDeuRed(Boolean deuRed) {
		this.deuRed = deuRed;
	}

	public Boolean getStautsLance() {
		return stautsLance;
	}

	public void setStautsLance(Boolean stautsLance) {
		this.stautsLance = stautsLance;
	}
	
	
	
	
	
}
