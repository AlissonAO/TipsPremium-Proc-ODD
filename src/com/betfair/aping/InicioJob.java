package com.betfair.aping;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.betfair.aping.api.ApiNgJsonRpcOperations;
import com.betfair.aping.api.ApiNgOperations;
import com.betfair.aping.entities.MarketBook;
import com.betfair.aping.entities.MarketCatalogue;
import com.betfair.aping.entities.MarketFilter;
import com.betfair.aping.entities.PriceProjection;
import com.betfair.aping.entities.PriceSize;
import com.betfair.aping.entities.ResultadoStatusCorridaVO;
import com.betfair.aping.entities.Runner;
import com.betfair.aping.entities.RunnerCatalog;
import com.betfair.aping.entities.TimeRange;
import com.betfair.aping.enums.MarketProjection;
import com.betfair.aping.enums.MarketSort;
import com.betfair.aping.enums.MarketStatus;
import com.betfair.aping.enums.MatchProjection;
import com.betfair.aping.enums.OrderProjection;
import com.betfair.aping.enums.PriceData;
import com.betfair.aping.enums.RunnerStatu;
import com.betfair.aping.exceptions.APINGException;
import com.betfair.aping.util.Data;

import betfair.dao.HistoricoCorridas;
import jdk.nashorn.internal.runtime.RewriteException;

public class InicioJob {

	private static ApiNgOperations jsonOperations = ApiNgJsonRpcOperations.getInstance();
	private static String applicationKey;
	private static String sessionToken;
	private static List<String> listCorridasFeitas;
	private static String TIPODEEVENTO = "4339"; // 7
	private static List<String> listMercado = Arrays.asList("WIN", "PLACE");
	public static List<String> listPais = Arrays.asList("GB", "IE");
	public static DecimalFormat df = new DecimalFormat("#.##");
	public static SimpleDateFormat sp = new SimpleDateFormat("HH:mm");

	public static void main(String[] args) throws Exception, APINGException {
		System.out.println("#### starting o robo de gravação");
		InicioJob main = new InicioJob();
		applicationKey = "XQzvGbEmSL9JwR7n";
		sessionToken = "nAVj9ZZ8XpdK/kZU4K39Yk8Hi9wwHjAGBWY67hOn3qI=";
		listCorridasFeitas = new ArrayList<String>();
		while (true) {
//			HistoricoCorridas h = new HistoricoCorridas(null);
			for (String pais : listPais) {

				new Thread() {
					public void run() {
						List<MarketCatalogue> retornoProximaCorridaList;
						retornoProximaCorridaList = main.obterProximaCorrida(pais);
//						h.altualizarODD(retornoProximaCorridaList, null);
						if (retornoProximaCorridaList != null && !retornoProximaCorridaList.isEmpty()) {
							if (!listCorridasFeitas.contains(retornoProximaCorridaList.get(0).getMarketId())) {
								listCorridasFeitas.add(retornoProximaCorridaList.get(0).getMarketId());
								boolean podeEntra = Boolean.FALSE;
								while (!podeEntra) {
									try {
										int hora = LocalDateTime.now().getHour();
										if (hora <= 18 && hora >= 04) {
											podeEntra = main.calcularTempoEntrada(retornoProximaCorridaList, pais);
											if (podeEntra) {
												InicioJob inicioJob = new InicioJob();
												inicioJob.cadastrarCorridaWin(retornoProximaCorridaList, pais);
											}
										} else {
											System.out.println("Aguardando a nova rodada");
											Thread.sleep(10000);
										}
									} catch (Exception e) {
										InicioJob.gravarLog(e.getMessage() + e.getCause());
										e.printStackTrace();
									} catch (APINGException e) {
										InicioJob.gravarLog(e.getMessage() + e.getCause() + e.fillInStackTrace().toString());
										e.printStackTrace();
									}
								}
							}
						}
					}
				}.start();
			}
			Thread.sleep(2500);
		}

	}

	protected void cadastrarCorridaWin(List<MarketCatalogue> proximaCorridaList, String pais) throws APINGException, InterruptedException {
		List<MarketBook> marketBookReturn = null;
		PriceProjection priceProjection = new PriceProjection();
		Set<PriceData> priceData = new HashSet<PriceData>();
		priceData.add(PriceData.EX_ALL_OFFERS);
		priceData.add(PriceData.EX_BEST_OFFERS);
		priceData.add(PriceData.EX_TRADED);
		priceData.add(PriceData.SP_AVAILABLE);
		priceData.add(PriceData.SP_TRADED);
		priceProjection.setPriceData(priceData);

		// In this case we don't need these objects so they are declared null
		OrderProjection orderProjection = null;
		MatchProjection matchProjection = null;
		String currencyCode = null;
		Map<Long, Runner> listaRunnerMap = null;
		Map<Long, Runner> listaRunnerMapPlace = null;
		Double totalCorrida = 0.0;
		List<String> marketIds = new ArrayList<String>();
		if(proximaCorridaList.size() >=2) {
			if (!proximaCorridaList.get(0).getMarketId().isEmpty() ? marketIds.add(proximaCorridaList.get(0).getMarketId()) : null);
			if (!proximaCorridaList.get(1).getMarketId().isEmpty() ? marketIds.add(proximaCorridaList.get(1).getMarketId()) : null);
		}else {
			if (!proximaCorridaList.get(0).getMarketId().isEmpty() ? marketIds.add(proximaCorridaList.get(0).getMarketId()) : null);
		}
		boolean status = false;
		System.out.println("Verificando Status corrida");
		listaRunnerMap = new HashMap<Long, Runner>();
		listaRunnerMapPlace = new HashMap<Long, Runner>();
		String date = sp.format(proximaCorridaList.get(0).getMarketStartTime());
		while (!status) {
			try {
				marketBookReturn = jsonOperations.listMarketBook(marketIds, priceProjection, orderProjection, matchProjection, currencyCode, applicationKey, sessionToken);
				if (marketBookReturn != null && !marketBookReturn.isEmpty()) {
					if (marketBookReturn.get(0).getStatus().equals(MarketStatus.OPEN.name())) {
						boolean posicao = Boolean.FALSE;
						for (int i = 0; i < marketBookReturn.size(); ++i) {
							System.out.print("[2] A corrida esta ABERTA " + proximaCorridaList.get(0).getEvent().getVenue() + " " +  proximaCorridaList.get(0).getMarketName() + " Hora " + 
																											date + " \n" );
							Runner retorno = null;
							for (RunnerCatalog r : proximaCorridaList.get(i).getRunners()) {
								if (proximaCorridaList.get(i).getDescription().getMarketType().equals("PLACE") || proximaCorridaList.get(i).getMarketName().equals("Posição")) {
									posicao = Boolean.TRUE;
								}
								for (Runner runner : marketBookReturn.get(i).getRunners()) {
									totalCorrida = marketBookReturn.get(i).getTotalMatched();
									//só pega quem estiver ativo
									if(runner.getStatus().equals(RunnerStatu.ACTIVE.name())) {
										if (r.getSelectionId().equals(runner.getSelectionId())) {
											retorno = new Runner();
											retorno.setSelectionId(r.getSelectionId());
											retorno.setName(r.getRunnerName().substring(3));
											retorno.setTrap(Integer.valueOf(r.getRunnerName().substring(0, 1)));
											if (runner.getTotalMatched() != null) {
												retorno.setTotalMatched(runner.getTotalMatched());
											}
											//pegar as ODD BAck
											for (PriceSize ex : runner.getEx().getAvailableToLay()) {
												BigDecimal bd = new BigDecimal((1 / ex.getPrice().doubleValue() * 100)).setScale(2, RoundingMode.HALF_UP);
												retorno.setProbabilidade(bd.doubleValue());
												retorno.setOldLay(ex.getPrice());
												break;
											}
											//pegar as ODD Lay
											for (PriceSize ex : runner.getEx().getAvailableToBack()) {
												retorno.setOldBack(ex.getPrice());
												break;
											}
											// System.out.println("Verificando o resultado galgo " + r.getRunnerName() + " "
											// + runner.getTotalMatched() + " Back " + retorno.getOldBack() + " Lay "
											// + retorno.getOldLay());
										}
									}

								}
								if (!posicao) {
									listaRunnerMap.put(r.getSelectionId(), retorno);
								} else {
									listaRunnerMapPlace.put(r.getSelectionId(), retorno);
								}
							}
						}

					} else if (marketBookReturn.get(0).getStatus().equals(MarketStatus.SUSPENDED.name()) && !listaRunnerMap.isEmpty()) {
						boolean posicao = Boolean.FALSE;
						System.out.print("[3] A corrida esta SUSPENSA " + proximaCorridaList.get(0).getEvent().getVenue() + " " +  proximaCorridaList.get(0).getMarketName() + " Hora " + 
																										date + " \n" );
						for (int i = 0; i < marketBookReturn.size(); ++i) {
							for (RunnerCatalog r : proximaCorridaList.get(i).getRunners()) {
								if (proximaCorridaList.get(i).getDescription().getMarketType().equals("PLACE") || proximaCorridaList.get(i).getMarketName().equals("Posição")) {
									posicao = Boolean.TRUE;
								}
								for (Runner runner : marketBookReturn.get(i).getRunners()) {
									if (r.getSelectionId().equals(runner.getSelectionId()) && runner.getStatus().equals(RunnerStatu.ACTIVE.name())) {
										if (runner.getTotalMatched() != null) {
											// System.out.println("Verificando o total galgo " + r.getRunnerName() + " " +
											// runner.getTotalMatched());
											if (!posicao) {
												Runner retornoMap = listaRunnerMap.get(r.getSelectionId());
												Runner retornoMapNovo = retornoMap;
												if (runner.getTotalMatched() != null) {
													retornoMapNovo.setTotalMatched(runner.getTotalMatched());
												}

											} else {
												Runner retornoMap = listaRunnerMapPlace.get(r.getSelectionId());
												Runner retornoMapNovo = retornoMap;
												if (runner.getTotalMatched() != null) {
													retornoMapNovo.setTotalMatched(runner.getTotalMatched());
												}
											}
											// System.out.println("Verificando o resultado galgo " + r.getRunnerName() + " "
											// + runner.getTotalMatched());
										}
									}

								}
							}
						}

					} else if (marketBookReturn.get(0).getStatus().equals(MarketStatus.CLOSED.name()) && !listaRunnerMap.isEmpty()) {
						boolean posicao = false;
						System.out.print("[4] A corrida esta FECHADA coletando informaçoes " + proximaCorridaList.get(0).getEvent().getVenue() + " " +  proximaCorridaList.get(0).getMarketName() + " Hora " + 
																										date + " \n" );
						for (int i = 0; i < marketBookReturn.size(); ++i) {
							for (RunnerCatalog r : proximaCorridaList.get(i).getRunners()) {
								System.out.println("Nome corrida " + proximaCorridaList.get(i).getMarketName());
								if (proximaCorridaList.get(i).getDescription().getMarketType().equals("PLACE") || proximaCorridaList.get(i).getMarketName().equals("To Be Placed") || proximaCorridaList.get(i).getMarketName().equals("Posição")) {
									posicao = Boolean.TRUE;
								}
								for (Runner runner : marketBookReturn.get(i).getRunners()) {
									if (r.getSelectionId().equals(runner.getSelectionId())) {
										if (!posicao) {
											if (runner.getStatus().equals(RunnerStatu.WINNER.name())) {
												Runner retornoMap = listaRunnerMap.get(r.getSelectionId());
												if (retornoMap != null) {
													Runner retornoMapNovo = retornoMap;
													retornoMapNovo.setWin(Boolean.TRUE);
													retornoMapNovo.setPlace(Boolean.TRUE);
													listaRunnerMap.replace(r.getSelectionId(), retornoMap, retornoMapNovo);
												}
											}
										} else {
											if (runner.getStatus().equals(RunnerStatu.WINNER.name())) {
												Runner retornoMap = listaRunnerMapPlace.get(r.getSelectionId());
												Runner retornoMapNovo = retornoMap;
												retornoMapNovo.setWin(Boolean.TRUE);
												listaRunnerMapPlace.replace(r.getSelectionId(), retornoMap, retornoMapNovo);
											}
										}
									}

								}
							}
						}
						HistoricoCorridas hist = new HistoricoCorridas(pais);
						ResultadoStatusCorridaVO resultVO =  hist.pesquisarCorrida(proximaCorridaList, totalCorrida);
						hist.cadastrarCorridas(listaRunnerMap, listaRunnerMapPlace, proximaCorridaList, resultVO);
						status = Boolean.TRUE;
					}
				}
				Thread.sleep(1000);

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
				InicioJob.gravarLog(e.getMessage() + e.getCause() + e.fillInStackTrace().toString());
			}

		}
	}


	@SuppressWarnings("deprecation")
	private Boolean calcularTempoEntrada(List<MarketCatalogue> retornoProximaCorridaList, String pais) throws InterruptedException {
		StringBuilder log = new StringBuilder();
		for (MarketCatalogue marketCatalogue : retornoProximaCorridaList) {
			Date horaCorrida = marketCatalogue.getMarketStartTime();
			System.out.println("### " + "A corrida  " + retornoProximaCorridaList.get(0).getEvent().getVenue() + "  "+ retornoProximaCorridaList.get(0).getMarketName() + " Começara em " + horaCorrida + "\n ");
			if (horaCorrida.getHours() == Data.addHoursToJavaUtilDate(new Date(), Data.hora).getHours()) {
				int minuto = horaCorrida.getMinutes() - Data.addHoursToJavaUtilDate(new Date(), Data.hora).getMinutes();
				if (minuto <= 5) {
					return true;
				}
			}
			if (horaCorrida.getTime() < Data.addHoursToJavaUtilDate(new Date(), Data.hora).getTime()) {
				return true;
			}
		}
		Thread.sleep(1500);
		return false;
	}

	private List<MarketCatalogue> obterProximaCorrida(String pais) {

		List<MarketCatalogue> listretorno = new ArrayList<>();
		for (String m : listMercado) {

			MarketFilter marketFilter = new MarketFilter();
			Set<String> eventTypeIds = new HashSet<>();
			List<MarketCatalogue> marketCatalogueResult = new ArrayList<>();
			String maxResults = "1";
			TimeRange time = new TimeRange();
			time.setFrom(new Date());
			eventTypeIds.add(TIPODEEVENTO);

			Set<String> countries = new HashSet<>();
			countries.add(pais);
			Set<String> typesCode = new HashSet<>();
			typesCode.add(m);

			marketFilter.setEventTypeIds(eventTypeIds);
			marketFilter.setMarketStartTime(time);
			marketFilter.setMarketCountries(countries);
			marketFilter.setMarketTypeCodes(typesCode);

			Set<MarketProjection> marketProjection = new HashSet<>();
			marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);
			marketProjection.add(MarketProjection.EVENT);
			marketProjection.add(MarketProjection.MARKET_START_TIME);
			marketProjection.add(MarketProjection.MARKET_DESCRIPTION);
			marketProjection.add(MarketProjection.COMPETITION);

			try {
				marketCatalogueResult = jsonOperations.listMarketCatalogue(marketFilter, marketProjection, MarketSort.FIRST_TO_START, maxResults, applicationKey, sessionToken);
				if (marketCatalogueResult != null && !marketCatalogueResult.isEmpty()) {
					listretorno.addAll(marketCatalogueResult);
				}
			} catch (APINGException e) {
				System.err.println(e.getCause());
				e.printStackTrace();
				InicioJob.gravarLog(e.getMessage() + e.getCause() + e.fillInStackTrace().toString());
			}
		}
		return listretorno;

	}

	public static void gravarLog(String msg) {
		System.out.print("################## " + msg + " ###################");
//	 Path path = null;
//	 Calendar calendar = Calendar.getInstance();
//	 Date tomorrow = calendar.getTime();
//	 SimpleDateFormat fd = new SimpleDateFormat("dd-MM");
//	 String strDate = fd.format(tomorrow);
//	 path = Paths.get("c:/log/Erro" + "-" + strDate + ".txt");
//		if (!Files.exists(path))
//			try {
//				Files.createFile(path);
//				List<String> linhas = Files.readAllLines(path);
//				linhas.add(msg);
//				Files.write(path, linhas);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
	}

}
