package com.betfair.teste;

import java.util.ArrayList;
import java.util.List;

import com.betfair.aping.entities.ExchangePrices;
import com.betfair.aping.entities.MarketBook;
import com.betfair.aping.entities.PriceSize;
import com.betfair.aping.entities.Runner;
import com.betfair.aping.enums.RunnerStatu;

public class MarketBookTest {

	public double price = 2.0;
	public double size = 10.0;
	
	public List<MarketBook> obterMarket() {
		List<MarketBook> listRetorno = new ArrayList<MarketBook>();
		MarketBook mark = new MarketBook();
		mark.setMarketId("1.3333333");
		mark.setNumberOfActiveRunners(6);
		mark.setNumberOfRunners(6);
		mark.setRunners(obterListRunner());
		mark.setStatus("CLOSED");
		listRetorno.add(mark);
		return listRetorno;

	}

	private List<Runner> obterListRunner() {
		List<Runner> listRunner = new ArrayList<Runner>();
		Runner r = new Runner();
		r.setSelectionId(1234567L);
		r.setEx(obterExchanger());
		r.setStatus(RunnerStatu.WINNER.name());
		listRunner.add(r);

		Runner r2 = new Runner();
		r2.setSelectionId(123456L);
		r2.setEx(obterExchanger());
		r2.setStatus(RunnerStatu.LOSER.name());
		listRunner.add(r2);
		
		Runner r3 = new Runner();
		r3.setSelectionId(123456L);
		r3.setEx(obterExchanger());
		r3.setStatus(RunnerStatu.LOSER.name());
		listRunner.add(r3);
		
		Runner r4 = new Runner();
		r4.setSelectionId(123456L);
		r4.setEx(obterExchanger());
		r4.setStatus(RunnerStatu.LOSER.name());
		listRunner.add(r4);
		
		Runner r5 = new Runner();
		r5.setSelectionId(123456L);
		r5.setEx(obterExchanger());
		r5.setStatus(RunnerStatu.LOSER.name());
		listRunner.add(r5);
		
		Runner r6 = new Runner();
		r6.setSelectionId(123456L);
		r6.setEx(obterExchanger());
		r6.setStatus(RunnerStatu.LOSER.name());
		listRunner.add(r6);
		
		return listRunner;
	}

	private ExchangePrices obterExchanger() {
		ExchangePrices ex = new ExchangePrices();
		ex.setAvailableToLay(obterListExcha());
		return ex;
	}

	private List<PriceSize> obterListExcha() {
		List<PriceSize> lisretorno = new ArrayList<PriceSize>();
		PriceSize p = new PriceSize();
		p.setPrice(price);
		p.setSize(size);
		lisretorno.add(p);

		return lisretorno;
	}

}