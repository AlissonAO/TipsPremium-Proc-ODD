package com.betfair.aping.enums;

public enum ApiNgOperation {
	LISTEVENTTYPES("listEventTypes"), 
	LISTCOMPETITIONS("listCompetitions"),
	LISTTIMERANGES("listTimeRanges"),
	LISTEVENTS("listEvents"),
	LISTMARKETTYPES("listMarketTypes"),
	LISTCOUNTRIES("listCountries"),
	LISTVENUES("listVenues"),
	LISTMARKETCATALOGUE("listMarketCatalogue"),
	LISTMARKETBOOK("listMarketBook"),
	PLACORDERS("placeOrders"),
	LIST_CURRENT_ORDERS_METHOD("listCurrentOrders"),
	CANCEL_ORDERS("cancelOrders"),
	REPLACE_ORDERS_METHOD("replaceOrders");
	
	private String operationName;
	
	private ApiNgOperation(String operationName){
		this.operationName = operationName;
	}

	public String getOperationName() {
		return operationName;
	}

	

}
