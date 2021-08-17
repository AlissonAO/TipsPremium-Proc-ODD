package com.betfair.aping.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.betfair.aping.auth.AppKeyAndSessionProvider;
import com.betfair.aping.auth.InvalidCredentialException;
import com.betfair.aping.containers.CancelerOrdersContainer;
import com.betfair.aping.containers.EventTypeResultContainer;
import com.betfair.aping.containers.ListMarketBooksContainer;
import com.betfair.aping.containers.ListMarketCatalogueContainer;
import com.betfair.aping.containers.PlaceOrdersContainer;
import com.betfair.aping.containers.ReplaceOrdersContainer;
import com.betfair.aping.entities.CancelExecutionReport;
import com.betfair.aping.entities.CancelInstruction;
import com.betfair.aping.entities.CurrentOrderSummary;
import com.betfair.aping.entities.EventTypeResult;
import com.betfair.aping.entities.MarketBook;
import com.betfair.aping.entities.MarketCatalogue;
import com.betfair.aping.entities.MarketFilter;
import com.betfair.aping.entities.PlaceExecutionReport;
import com.betfair.aping.entities.PlaceInstruction;
import com.betfair.aping.entities.PriceProjection;
import com.betfair.aping.entities.ReplaceExecutionReport;
import com.betfair.aping.entities.ReplaceInstruction;
import com.betfair.aping.enums.ApiNgOperation;
import com.betfair.aping.enums.MarketProjection;
import com.betfair.aping.enums.MarketSort;
import com.betfair.aping.enums.MatchProjection;
import com.betfair.aping.enums.OrderProjection;
import com.betfair.aping.exceptions.APINGException;
import com.betfair.aping.util.JsonConverter;
import com.betfair.aping.util.JsonrpcRequest;

public class ApiNgJsonRpcOperations extends ApiNgOperations {

	private static ApiNgJsonRpcOperations instance = null;

	private AppKeyAndSessionProvider sessionProvider;

	String appkey;
	String token;

	private ApiNgJsonRpcOperations() {

		newSessionProvider();
	}

	public static ApiNgJsonRpcOperations getInstance() {
		if (instance == null) {
			instance = new ApiNgJsonRpcOperations();
		}

		return instance;
	}

	private void newSessionProvider() {
		sessionProvider = new AppKeyAndSessionProvider();
	}

	public List<EventTypeResult> listEventTypes(MarketFilter filter, String appKey, String ssoId)
			throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(FILTER, filter);
		params.put(LOCALE, locale);
		String result = getInstance().makeRequest(ApiNgOperation.LISTEVENTTYPES.getOperationName(), params, this.appkey,
				this.token);
		EventTypeResultContainer container = JsonConverter.convertFromJson(result, EventTypeResultContainer.class);
		if (container.getError() != null)
			throw container.getError().getData().getAPINGException();

		return container.getResult();

	}

	public List<MarketBook> listMarketBook(List<String> marketIds, PriceProjection priceProjection,
			OrderProjection orderProjection, MatchProjection matchProjection, String currencyCode, String appKey,
			String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(MARKET_IDS, marketIds);
		params.put(PRICE_PROJECTION, priceProjection);
		params.put(ORDER_PROJECTION, orderProjection);
		params.put(MATCH_PROJECTION, matchProjection);
		params.put("currencyCode", currencyCode);
		String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETBOOK.getOperationName(), params, this.appkey,
				this.token);
		ListMarketBooksContainer container = null;
		try {
			container = JsonConverter.convertFromJson(result, ListMarketBooksContainer.class);

			if (container == null) {
				ApiNgJsonRpcOperations name = new ApiNgJsonRpcOperations();
				name.listMarketBook(marketIds, priceProjection, orderProjection, matchProjection, currencyCode,
						this.appkey, this.token);
			} else {

			}
			if (container != null) {
				if (container.getError() != null) {

					throw container.getError().getData().getAPINGException();
				}
			}

			return container.getResult();
		} catch (NullPointerException e) {
			ApiNgJsonRpcOperations name = new ApiNgJsonRpcOperations();
			name.listMarketBook(marketIds, priceProjection, orderProjection, matchProjection, currencyCode, appkey,
					token);
		}

		return null;
	}

	public List<MarketCatalogue> listMarketCatalogue(MarketFilter filter, Set<MarketProjection> marketProjection,
			MarketSort sort, String maxResult, String appKeyy, String ssoIdy) throws APINGException {

		try {
			sessionProvider.getOrCreateNewSession();
			appkey = sessionProvider.getOrCreateNewSession().getAppkey();
			token = sessionProvider.getOrCreateNewSession().getSession();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidCredentialException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(FILTER, filter);
		params.put(SORT, sort);
		params.put(MAX_RESULT, maxResult);
		params.put(MARKET_PROJECTION, marketProjection);
		String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETCATALOGUE.getOperationName(), params,
				this.appkey, this.token);
		ListMarketCatalogueContainer container = JsonConverter.convertFromJson(result,
				ListMarketCatalogueContainer.class);

//		if(container == null) {
//			listMarketCatalogue( filter,  marketProjection,
//					 sort,  maxResult,  appKeyy,  ssoIdy) ;
//		}else {
//			if (container.getError() != null) {
//				throw container.getError().getData().getAPINGException();
//			}
//		}

		if (container != null && container.getResult() != null) {
			if (container.getError() != null) {
				throw container.getError().getData().getAPINGException();
			}
//			container.getResult().get(0).setMarketStartTime(addHoursToJavaUtilDate(container.getResult().get(0).getMarketStartTime(), 3));	
			return container.getResult();

		} else {
			List<MarketCatalogue> conteinerlist = new ArrayList<MarketCatalogue>();
			return conteinerlist;
		}

	}
	
	public Date addHoursToJavaUtilDate(Date date, int hours) {
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.HOUR_OF_DAY, -hours);
		    return calendar.getTime();
		}

	public PlaceExecutionReport placeOrders(String marketId, List<PlaceInstruction> instructions, String customerRef,
			String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(MARKET_ID, marketId);
		params.put(INSTRUCTIONS, instructions);
		params.put(CUSTOMER_REF, customerRef);
		String result = getInstance().makeRequest(ApiNgOperation.PLACORDERS.getOperationName(), params, this.appkey,
				this.token);
		PlaceOrdersContainer container = JsonConverter.convertFromJson(result, PlaceOrdersContainer.class);

		if (container.getError() != null)
			throw container.getError().getData().getAPINGException();

		return container.getResult();

	}

	public CancelExecutionReport cancelerOrder(String marketId, List<CancelInstruction> instructions,
			String customerRef, String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(MARKET_ID, marketId);
		params.put(INSTRUCTIONS, instructions);
		params.put(CUSTOMER_REF, customerRef);
		String result = getInstance().makeRequest(ApiNgOperation.CANCEL_ORDERS.getOperationName(), params, appkey,
				token);
		CancelerOrdersContainer container = JsonConverter.convertFromJson(result, CancelerOrdersContainer.class);

		if (container.getError() != null)
			throw container.getError().getData().getAPINGException();

		return container.getResult();

	}

	protected String makeRequest(String operation, Map<String, Object> params, String appKey, String ssoToken) {
		String requestString;
		// Handling the JSON-RPC request
		JsonrpcRequest request = new JsonrpcRequest();
		request.setId("1");
		request.setMethod("SportsAPING/v1.0/" + operation);
		request.setParams(params);

		requestString = JsonConverter.convertToJson(request);
		// We need to pass the "sendPostRequest" method a string in util format:
		// requestString
		HttpUtil requester = new HttpUtil();
		return requester.sendPostRequestJsonRpc(requestString, operation, appkey, token);

	}

	public List<CurrentOrderSummary> listCurrentOrders(Set<String> betIds, Set<String> marketIds, String appKey,
			String ssoId) throws APINGException {
		
		
		HashMap<String, Object> params = new HashMap<>();
		ApiNgRescriptOperations tr = new ApiNgRescriptOperations().getInstance();
		List<CurrentOrderSummary> retorno =  tr.listCurrentOrders(betIds, marketIds, appkey, token);
		

//		 params.put(BET_IDS, betIds);
//		params.put(MARKET_IDS, marketIds);
//		//params.put(ORDER_PROJECTION, orderProjection);
//		// params.put(DATE_RANGE, dateRange);
//		// params.put(PLACED_DATE_RANGE, placedDateRange);
//	
//
//		// params.put(ORDER_PROJECTION, orderProjection);
//		String result = getInstance().makeRequest(ApiNgOperation.LIST_CURRENT_ORDERS_METHOD.getOperationName(), params,
//				appkey, token);
//
//		if (ApiNGDemo.isDebug())
//			System.out.println("\nResponse: " + result);
//
//		CurrentOrderSummaryReport container = JsonConverter.convertFromJson(result, CurrentOrderSummaryReport.class);
//
//		if (container.getError() != null)
//			throw container.getError().getData().getAPINGException();

		return retorno;

	}

	@Override
	public ReplaceExecutionReport replaceOrders(String marketId, List<ReplaceInstruction> instructions,
			String customerRef, String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(MARKET_ID, marketId);
		params.put(INSTRUCTIONS, instructions);
		params.put(CUSTOMER_REF, customerRef);
		String result = getInstance().makeRequest(ApiNgOperation.REPLACE_ORDERS_METHOD.getOperationName(), params,
				appkey, token);
		
		ReplaceOrdersContainer container = JsonConverter.convertFromJson(result, ReplaceOrdersContainer.class);

		if (container.getError() != null)
			throw container.getError().getData().getAPINGException();

		return container.getResult();

	}

}
