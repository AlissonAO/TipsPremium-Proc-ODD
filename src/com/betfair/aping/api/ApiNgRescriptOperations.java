package com.betfair.aping.api;

import com.betfair.aping.InicioJob;
import com.betfair.aping.containers.CurrentOrderSummaryReport;
import com.betfair.aping.entities.*;
import com.betfair.aping.enums.*;
import com.betfair.aping.exceptions.APINGException;
import com.betfair.aping.util.JsonConverter;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ApiNgRescriptOperations extends ApiNgOperations {

    private static ApiNgRescriptOperations instance = null;

    ApiNgRescriptOperations(){}

    public static ApiNgRescriptOperations getInstance(){
        if (instance == null){
            instance = new ApiNgRescriptOperations();
        }
        return instance;
    }

    public List<EventTypeResult> listEventTypes(MarketFilter filter, String appKey, String ssoId) throws APINGException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FILTER, filter);
        params.put(LOCALE, locale);
        String result = getInstance().makeRequest(ApiNgOperation.LISTEVENTTYPES.getOperationName(), params, appKey, ssoId);
       
        List<EventTypeResult> container = JsonConverter.convertFromJson(result, new TypeToken<List<EventTypeResult>>() {}.getType());

        return container;

    }

    public List<MarketBook> listMarketBook(List<String> marketIds, PriceProjection priceProjection, OrderProjection orderProjection,
                                           MatchProjection matchProjection, String currencyCode, String appKey, String ssoId) throws APINGException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(LOCALE, locale);
        params.put(MARKET_IDS, marketIds);
        params.put(PRICE_PROJECTION, priceProjection);
        params.put(ORDER_PROJECTION, orderProjection);
        params.put(MATCH_PROJECTION, matchProjection);
        String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETBOOK.getOperationName(), params, appKey, ssoId);
      
        List<MarketBook> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketBook>>(){}.getType() );

        return container;

    }

    public List<MarketCatalogue> listMarketCatalogue(MarketFilter filter, Set<MarketProjection> marketProjection,
                                                     MarketSort sort, String maxResult, String appKey, String ssoId) throws APINGException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(LOCALE, locale);
        params.put(FILTER, filter);
        params.put(SORT, sort);
        params.put(MAX_RESULT, maxResult);
        params.put(MARKET_PROJECTION, marketProjection);
        String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETCATALOGUE.getOperationName(), params, appKey, ssoId);
        

        List<MarketCatalogue> container = JsonConverter.convertFromJson(result, new TypeToken< List<MarketCatalogue>>(){}.getType() );

        return container;

    }

    public PlaceExecutionReport placeOrders(String marketId, List<PlaceInstruction> instructions, String customerRef , String appKey, String ssoId) throws APINGException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(LOCALE, locale);
        params.put(MARKET_ID, marketId);
        params.put(INSTRUCTIONS, instructions);
        params.put(CUSTOMER_REF, customerRef);
        String result = getInstance().makeRequest(ApiNgOperation.PLACORDERS.getOperationName(), params, appKey, ssoId);
        

        return JsonConverter.convertFromJson(result, PlaceExecutionReport.class);

    }


    protected String makeRequest(String operation, Map<String, Object> params, String appKey, String ssoToken) throws APINGException {
        String requestString;
        //Handling the Rescript request
        params.put("id", 1);

        requestString =  JsonConverter.convertToJson(params);
       

        //We need to pass the "sendPostRequest" method a string in util format:  requestString
        HttpUtil requester = new HttpUtil();
        String response = requester.sendPostRequestRescript(requestString, operation, appKey, ssoToken);
        if(response != null)
            return response;
        else
            throw new APINGException();
    }

    public List<CurrentOrderSummary> listCurrentOrders(Set<String> betIds, Set<String> marketIds,  String appKey, String ssoId ) throws APINGException {
        
        HashMap<String, Object> params = new HashMap<>();
        params.put(BET_IDS, betIds);
        params.put(MARKET_IDS, marketIds);
     //   params.put(ORDER_PROJECTION, orderProjection);
        String result = getInstance().makeRequest(ApiNgOperation.LIST_CURRENT_ORDERS_METHOD.getOperationName(), params, appKey, ssoId);
		
       
        CurrentOrderSummaryReport container = JsonConverter.convertFromJson(result, CurrentOrderSummaryReport.class);

        if(container.getError() != null)
            throw container.getError().getData().getAPINGException();

        return container.getCurrentOrders();
        
        
    }

	@Override
	public CancelExecutionReport cancelerOrder(String marketId, List<CancelInstruction> instructions,
			String customerRef, String appKey, String ssoId) throws APINGException {
		// TODO Auto-generated method stub
		return null;
	}

	public ReplaceExecutionReport replaceOrders(String marketId, List<ReplaceInstruction> instructions,
			String customerRef) throws APINGException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReplaceExecutionReport replaceOrders(String marketId, List<ReplaceInstruction> instructions,
			String customerRef, String appKey, String ssoId) throws APINGException {
		// TODO Auto-generated method stub
		return null;
	}
}

