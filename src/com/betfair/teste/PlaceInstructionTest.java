package com.betfair.teste;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.betfair.aping.entities.PlaceExecutionReport;
import com.betfair.aping.entities.PlaceInstructionReport;
import com.betfair.aping.enums.ExecutionReportStatus;
import com.betfair.aping.enums.InstructionReportStatus;

public class PlaceInstructionTest {

	
	public PlaceExecutionReport obterOrder() {
		
		PlaceExecutionReport place = new PlaceExecutionReport();
		place.setMarketId("123");
		place.setStatus(ExecutionReportStatus.SUCCESS);
		place.setInstructionReports(obterPlace());
		
		return place;
		
	}

	private List<PlaceInstructionReport> obterPlace() {
		List<PlaceInstructionReport> listRetorno = new ArrayList<PlaceInstructionReport>();
		PlaceInstructionReport place = new PlaceInstructionReport();
		place.setBetId("123");
		place.setStatus(InstructionReportStatus.SUCCESS);
		place.setPlacedDate(new Date());
		place.setSizeMatched(2.0);
		listRetorno.add(place);
		
		return listRetorno;
	}
}