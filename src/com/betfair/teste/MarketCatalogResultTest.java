package com.betfair.teste;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.betfair.aping.entities.Competition;
import com.betfair.aping.entities.Event;
import com.betfair.aping.entities.MarketCatalogue;
import com.betfair.aping.entities.RunnerCatalog;

public class MarketCatalogResultTest {

	public List<MarketCatalogue> obterMarketCatalog() {

		List<MarketCatalogue> lisRetorno = new ArrayList<MarketCatalogue>();
		long teste = new Date().getTime() + 60000L;
		Date dataEntrada = new Date();
		dataEntrada.setTime(teste);
		MarketCatalogue ma = new MarketCatalogue();
		ma.setMarketId(String.valueOf(dataEntrada.getMinutes()));
		ma.setCompetition(obterCompetition());
		ma.setMarketStartTime(dataEntrada);
		ma.setEvent(ObterEvent());;
		ma.setMarketName(String.valueOf(dataEntrada.getMinutes()));
		ma.setRunners(obterRunner());

		lisRetorno.add(ma);
		return lisRetorno;

	}

	private Event ObterEvent() {
		Event retorno = new Event();
		retorno.setTimezone(new Date().toString());
		retorno.setName("Nott 10 Julho");
		return retorno;
	}

	private List<RunnerCatalog> obterRunner() {
		List<RunnerCatalog> lisRetorno = new ArrayList<RunnerCatalog>();
		List<String> listgalgos = obterNomeGalgo();
		for (String string : listgalgos) {
			RunnerCatalog r = new RunnerCatalog();
			r.setRunnerName("6. Roedhelm Madness");
			Random random = new Random();
			random.longs();
			r.setSelectionId(123456L);
			lisRetorno.add(r);

		}
		return lisRetorno;
	}

	private List<String> obterNomeGalgo() {

		List<String> listRetorno = new ArrayList<String>();
		listRetorno.add("1. Roedhelm Madness");
		listRetorno.add("2. Roedhelm Madness");
		listRetorno.add("3. Roedhelm Madness");
		listRetorno.add("4. Roedhelm Madness");
		listRetorno.add("5. Roedhelm Madness");
		listRetorno.add("6. Roedhelm Madness");

		return listRetorno;
	}

	private Competition obterCompetition() {
		Competition com = new Competition();
		com.setId("123456");
		com.setName("teste");
		return com;
	}

}