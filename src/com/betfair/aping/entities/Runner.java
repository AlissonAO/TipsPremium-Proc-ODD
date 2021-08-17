package com.betfair.aping.entities;

import java.util.Date;
import java.util.List;

public class Runner {
	private Long selectionId;
	private Double handicap;
	private String status;
	private Double adjustmentFactor;
	private Double lastPriceTraded;
	private Double totalMatched;
	private Date removalDate;
	private StartingPrices sp;
	private ExchangePrices ex;
	private List<Order> orders;
	private List<Match> matches;
	private String name;
	private Double oldLay = 0.0;
	private Double oldBack = 0.0;
	private Integer trap;
	private Boolean win = Boolean.FALSE;
	private Boolean place = Boolean.FALSE;
	private Double probabilidade = 0.0;
	private Double totalApostado = 0.0;
	
	
	public Boolean getPlace() {
		return place;
	}

	public void setPlace(Boolean place) {
		this.place = place;
	}

	public Double getTotalApostado() {
		return totalApostado;
	}

	public void setTotalApostado(Double totalApostado) {
		this.totalApostado = totalApostado;
	}

	public Double getProbabilidade() {
		return probabilidade;
	}

	public void setProbabilidade(Double probabilidade) {
		this.probabilidade = probabilidade;
	}

	public Boolean getWin() {
		return win;
	}

	public void setWin(Boolean win) {
		this.win = win;
	}

	public Integer getTrap() {
		return trap;
	}

	public void setTrap(Integer trap) {
		this.trap = trap;
	}

	public Long getSelectionId() {
		return selectionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getOldLay() {
		return oldLay;
	}

	public void setOldLay(Double oldLay) {
		this.oldLay = oldLay;
	}

	public Double getOldBack() {
		return oldBack;
	}

	public void setOldBack(Double oldBack) {
		this.oldBack = oldBack;
	}

	public void setSelectionId(Long selectionId) {
		this.selectionId = selectionId;
	}

	public Double getHandicap() {
		return handicap;
	}

	public void setHandicap(Double handicap) {
		this.handicap = handicap;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAdjustmentFactor() {
		return adjustmentFactor;
	}

	public void setAdjustmentFactor(Double adjustmentFactor) {
		this.adjustmentFactor = adjustmentFactor;
	}

	public Double getLastPriceTraded() {
		return lastPriceTraded;
	}

	public void setLastPriceTraded(Double lastPriceTraded) {
		this.lastPriceTraded = lastPriceTraded;
	}

	public Double getTotalMatched() {
		return totalMatched;
	}

	public void setTotalMatched(Double totalMatched) {
		this.totalMatched = totalMatched;
	}

	public Date getRemovalDate() {
		return removalDate;
	}

	public void setRemovalDate(Date removalDate) {
		this.removalDate = removalDate;
	}

	public StartingPrices getSp() {
		return sp;
	}

	public void setSp(StartingPrices sp) {
		this.sp = sp;
	}

	public ExchangePrices getEx() {
		return ex;
	}

	public void setEx(ExchangePrices ex) {
		this.ex = ex;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public String toString() {
		return "{" + "" + "selectionId=" + getSelectionId() + "," + "handicap="
				+ getHandicap() + "," + "status=" + getStatus() + ","
				+ "adjustmentFactor=" + getAdjustmentFactor() + ","
				+ "lastPriceTraded=" + getLastPriceTraded() + ","
				+ "totalMatched=" + getTotalMatched() + "," + "removalDate="
				+ getRemovalDate() + "," + "sp=" + getSp() + "," + "ex="
				+ getEx() + "," + "orders=" + getOrders() + "," + "matches="
				+ getMatches() + "," + "}";
	}

}
