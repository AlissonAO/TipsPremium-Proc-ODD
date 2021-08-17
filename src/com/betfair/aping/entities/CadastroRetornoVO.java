package com.betfair.aping.entities;

import java.util.List;
import java.util.Map;

public class CadastroRetornoVO {

	private Map<Long, Runner> listaRunnerMap;
	private List<MarketCatalogue> listPista;
	private Double totalCorrida;
	private String mercado;
	
	
	public String getMercado() {
		return mercado;
	}
	public void setMercado(String mercado) {
		this.mercado = mercado;
	}
	public Map<Long, Runner> getListaRunnerMap() {
		return listaRunnerMap;
	}
	public void setListaRunnerMap(Map<Long, Runner> listaRunnerMap) {
		this.listaRunnerMap = listaRunnerMap;
	}
	public List<MarketCatalogue> getListPista() {
		return listPista;
	}
	public void setListPista(List<MarketCatalogue> listPista) {
		this.listPista = listPista;
	}
	public Double getTotalCorrida() {
		return totalCorrida;
	}
	public void setTotalCorrida(Double totalCorrida) {
		this.totalCorrida = totalCorrida;
	}
	

	
	
	
	
}
