package com.betfair.aping.containers;


import com.betfair.aping.entities.CancelExecutionReport;

public class CancelerOrdersContainer extends Container {

	private CancelExecutionReport result;
	
	public CancelExecutionReport getResult() {
		return result;
	}
	
	public void setResult(CancelExecutionReport result) {
		this.result = result;
	}

}
