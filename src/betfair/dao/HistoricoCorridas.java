package betfair.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.betfair.aping.InicioJob;
import com.betfair.aping.entities.MarketBook;
import com.betfair.aping.entities.MarketCatalogue;
import com.betfair.aping.entities.ResultadoStatusCorridaVO;
import com.betfair.aping.entities.Runner;
import com.betfair.aping.entities.RunnerCatalog;
import com.betfair.aping.util.Data;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import betfair.conection.ConnectionFactory;

public class HistoricoCorridas {

	public static DecimalFormat formato = new DecimalFormat("#.##");
	private String pais;

	public HistoricoCorridas(String pais) {
		this.pais = pais;
	}

	public Boolean cadastrarCorridas(Map<Long, Runner> listaRunnerMap, Map<Long, Runner> listaRunnerMapPlace, List<MarketCatalogue> listPista, ResultadoStatusCorridaVO resultVO) {
		Connection con = null;
		PreparedStatement prepsInsertProduct = null;
		if (resultVO.getStatus().equals(""))
			return Boolean.FALSE;
		String update = "UPDATE public.\"Hist_galgo_betfair\" SET datainicio = ?" + "," + "datafim = ?" + "," + "odd_lay = ?" + "," + "odd_back = ?" + "," + "probabilidade = ?" + ","
																						+ "din_investido = ?" + "," + "win = ?" + "," + "odd_back_place = ?" + ","
																						+ "odd_lay_place = ?" + "," + "probabilidade_place = ?" + "," + "din_investido_place = ?"
																						+ "," + "win_place = ?" + "WHERE id_hist_pista = ?" + " AND iddogbetfair = ?";
		try {
			Date dataNow = Data.addHoursToJavaUtilDate(new Date(), Data.hora);
			con = ConnectionFactory.getConection();
			if (!listaRunnerMapPlace.isEmpty()) {

				for (Runner r : listaRunnerMap.values()) {
					for (Runner place : listaRunnerMapPlace.values()) {
							if (r.getSelectionId().equals(place.getSelectionId())) {
								prepsInsertProduct = con.prepareStatement(update);
								prepsInsertProduct.setTimestamp(1, new Timestamp(Data.addHoursToJavaUtilDate(listPista.get(0).getMarketStartTime(), Data.hora).getTime()));
								prepsInsertProduct.setTimestamp(2, new Timestamp(dataNow.getTime()));
								prepsInsertProduct.setDouble(3, r.getOldLay() != null ? r.getOldLay() : null);
								prepsInsertProduct.setDouble(4, r.getOldBack() != null ? r.getOldBack() : null);
								prepsInsertProduct.setDouble(5, r.getProbabilidade() != null ? r.getProbabilidade() : null);
								prepsInsertProduct.setDouble(6, r.getTotalMatched() != null ? r.getTotalMatched() : null);
								prepsInsertProduct.setBoolean(7, r.getWin() );
								prepsInsertProduct.setDouble(8, place.getOldBack() != null ? place.getOldBack() : null);
								prepsInsertProduct.setDouble(9, place.getOldLay() != null ? place.getOldBack() : null);
								prepsInsertProduct.setDouble(10, place.getProbabilidade() != null ? place.getProbabilidade() : null);
								prepsInsertProduct.setDouble(11, place.getTotalMatched() != null ? place.getTotalMatched() : null);
								prepsInsertProduct.setBoolean(12, place.getWin() );
								prepsInsertProduct.setInt(13, Integer.valueOf(resultVO.getIdPk()));
								prepsInsertProduct.setString(14, r.getSelectionId().toString());
								prepsInsertProduct.executeUpdate();

							}
					}
				}
				// Sem mercado place
			} else {
				for (Runner r : listaRunnerMap.values()) {
					prepsInsertProduct = con.prepareStatement(update);
					prepsInsertProduct.setTimestamp(1, new Timestamp(Data.addHoursToJavaUtilDate(listPista.get(0).getMarketStartTime(), Data.hora).getTime()));
					prepsInsertProduct.setTimestamp(2, new Timestamp(dataNow.getTime()));
					prepsInsertProduct.setDouble(3, r.getOldLay() != null ? r.getOldLay() : null);
					prepsInsertProduct.setDouble(4, r.getOldBack() != null ? r.getOldBack() : null);
					prepsInsertProduct.setDouble(5, r.getProbabilidade() != null ? r.getProbabilidade() : null);
					prepsInsertProduct.setDouble(6, r.getTotalMatched() != null ? r.getTotalMatched() : null);
					prepsInsertProduct.setBoolean(7, r.getWin() );
					prepsInsertProduct.setDouble(8, 0);
					prepsInsertProduct.setDouble(9, 0);
					prepsInsertProduct.setDouble(10, 0);
					prepsInsertProduct.setDouble(11, 0);
					prepsInsertProduct.setBoolean(12, Boolean.FALSE);
					prepsInsertProduct.setInt(13, Integer.valueOf(resultVO.getIdPk()));
					prepsInsertProduct.setString(14, r.getSelectionId().toString());
						
					prepsInsertProduct.executeUpdate();

				}
			}

			altualizarODD(listPista, listaRunnerMap, listaRunnerMapPlace);

			return Boolean.TRUE;

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			InicioJob.gravarLog(e.getMessage() + e.getCause());
		} finally {
			try {
				if (prepsInsertProduct != null) {
					if (!prepsInsertProduct.isClosed()) {
						prepsInsertProduct.close();
					}
				}
				if (!con.isClosed()) {
					con.close();

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return Boolean.TRUE;

	}

	public void altualizarODD(List<MarketCatalogue> listPista, Map<Long, Runner> listaRunnerMap, Map<Long, Runner> listaRunnerMapPlace) {

		MongoClientURI uri = new MongoClientURI("mongodb+srv://tips:alisson123@cluster0.svlya.mongodb.net/tips?retryWrites=true&w=majority");
		MongoClient mongoClient = new MongoClient(uri);
		try {
			MongoDatabase database = mongoClient.getDatabase("premiumTips");
			MongoCollection<Document> collection = database.getCollection("historicoCalculado");
			BasicDBObject query = MontaQueryConsulta(listPista);
			System.out.println("###### " + query + " #######");
			try (MongoCursor<Document> cur = collection.find(query).iterator()) {
				BasicDBObject setQuery = new BasicDBObject();
				BasicDBObject updateFields = new BasicDBObject();
				while (cur.hasNext()) {
					Document doc = cur.next();
					List<Document> listdogs = (List<Document>) doc.get("dogs");
					for (Document dogMongoDB : listdogs) {
						for (Runner runner : listaRunnerMap.values()) {
							// Pegar pelo nome
							if (dogMongoDB.getString("nome").equals(runner.getName())) {
								// pegar o segundo colocado
								for (Runner place : listaRunnerMapPlace.values()) {
									if (dogMongoDB.getString("nome").equals(place.getName())) {
										if (place.getWin().equals(Boolean.TRUE)) {
											updateFields.append("dogs." + (place.getTrap().intValue() - 1) + ".resultado", "2");

										}
									}
								}

								if (runner.getWin().equals(Boolean.TRUE)) {
									updateFields.append("dogs." + (runner.getTrap().intValue() - 1) + ".resultado", "1");
									updateFields.append("statusResultado", "P");
								}
								updateFields.append("dogs." + (runner.getTrap().intValue() - 1) + ".odd_Back", runner.getOldBack());
								updateFields.append("dogs." + (runner.getTrap().intValue() - 1) + ".odd_Lay", runner.getOldLay());
								break;
							}
						}
					}
					setQuery.append("$set", updateFields);
	     			Document retorno = collection.findOneAndUpdate(query, setQuery);
	     			if(retorno != null) System.out.println("[6] Retorno Cadastro" + retorno.toJson());
				}

			}

//			BasicDBObject setQuery = new BasicDBObject();
//			BasicDBObject updateFields = new BasicDBObject();
//			for (Runner r : listaRunnerMap.values()) {
//				updateFields.append("dogs." + (r.getTrap().intValue()-1) + ".odd_Back", r.getOldBack());
//				updateFields.append("dogs." + (r.getTrap().intValue()-1) + ".odd_Lay", r.getOldLay());
//			}
//			setQuery.append("$set", updateFields);
//			Document retorno = collection.findOneAndUpdate(query, setQuery);
//			if(retorno != null) System.out.println("[6] Retorno Cadastro" + retorno.toJson());

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			InicioJob.gravarLog(e.getMessage() + e.getCause());
		} finally {
			mongoClient.close();
		}

	}

	private BasicDBObject MontaQueryConsulta(List<MarketCatalogue> listPista) throws ParseException {

		BasicDBObject retorno = new BasicDBObject();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date datafinal = format.parse(format.format(new Date()));
		for (MarketCatalogue marketCatalogue : listPista) {
			// Horario de verão adicionar mais uma hora no metodo
			String data = sdf.format(Data.addHoursToJavaUtilDate(marketCatalogue.getMarketStartTime(), Data.hora));
//			String data = sdf.format(marketCatalogue.getMarketStartTime());
//			retorno.put("Grade", "HP");
//			retorno.put("Dis", "500");
//			retorno.put("TrackName", "Sheffield");
//			retorno.put("HoraCorridaBR", "18:01");

			retorno.append("DataCorrida", new Document("$gte", datafinal));
//			String[] grade = marketCatalogue.getMarketName().split(" ");
//			if(!grade[0].equals("HP") && !grade[0].equals("HC")) {
//				retorno.append("Grade", grade[0]);
//			}
//			if (grade[1].contains("m")) {
//				grade[1] = grade[1].replace("m", "");
//			}else if ( grade[1].contains("M")) {
//				grade[1] = grade[1].replace("M", "");
//			}
//			retorno.append("Dis", grade[1]);
			retorno.append("TrackName", marketCatalogue.getEvent().getVenue());
			retorno.append("HoraCorridaBR", data);
//			retorno.append("HoraCorridaUK", data);
			break;
		}

		return retorno;
	}

	@SuppressWarnings("resource")
	public ResultadoStatusCorridaVO pesquisarCorrida(List<MarketCatalogue> listPista, Double totalCorrida, int qtdGalgo) {
		ResultSet resultSet = null;
		PreparedStatement prepsInsertProduct = null;
		ResultadoStatusCorridaVO retorno = new ResultadoStatusCorridaVO();
		Timestamp timestamp = new Timestamp(Data.addHoursToJavaUtilDate(listPista.get(0).getMarketStartTime(), Data.hora).getTime());
		String pesquisarCorrida = "SELECT id, statusresultado  from public.\"Hist_pista_betfair\" where pista = ? AND data_corrida = ?";

		String update = "UPDATE public.\"Hist_pista_betfair\" SET total_dinheiro = ?" + "," + "statusresultado = ?" + "," + "qtdgalgo = ?" + "WHERE id = ?";

		Connection con = null;
		try {
			con = ConnectionFactory.getConection();
			prepsInsertProduct = con.prepareStatement(pesquisarCorrida);
			prepsInsertProduct.setString(1, listPista.get(0).getEvent().getVenue());
			prepsInsertProduct.setTimestamp(2, timestamp);
			resultSet = prepsInsertProduct.executeQuery();
			while (resultSet.next()) {
				retorno.setIdPk(resultSet.getString(1));
				retorno.setStatus(resultSet.getString(2));
			}
			prepsInsertProduct = con.prepareStatement(update);
			prepsInsertProduct.setDouble(1, totalCorrida);
			if (retorno.getStatus().equals("O")) {
				prepsInsertProduct.setString(2, "P");
				retorno.setStatus("P");
			} else {
				prepsInsertProduct.setString(2, retorno.getStatus());
			}
			prepsInsertProduct.setInt(3, qtdGalgo);
			prepsInsertProduct.setInt(4, Integer.valueOf(retorno.getIdPk()));
			prepsInsertProduct.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
			InicioJob.gravarLog(e.getMessage() + e.getCause());
		} finally {
			try {
				if (prepsInsertProduct != null) {
					if (!prepsInsertProduct.isClosed()) {
						prepsInsertProduct.close();
					}
				}
				if (!con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retorno;
	}

	
	public ResultadoStatusCorridaVO pesquisarCorrida(List<MarketCatalogue> listPista) {
		ResultSet resultSet = null;
		PreparedStatement prepsInsertProduct = null;
		ResultadoStatusCorridaVO retorno = new ResultadoStatusCorridaVO();
		Timestamp timestamp = new Timestamp(Data.addHoursToJavaUtilDate(listPista.get(0).getMarketStartTime(), Data.hora).getTime());
		String pesquisarCorrida = "SELECT id, statusresultado  from public.\"Hist_pista_betfair\" where pista = ? AND data_corrida = ?";
		Connection con = null;
		try {
			con = ConnectionFactory.getConection();
			prepsInsertProduct = con.prepareStatement(pesquisarCorrida);
			prepsInsertProduct.setString(1, listPista.get(0).getEvent().getVenue());
			prepsInsertProduct.setTimestamp(2, timestamp);
			resultSet = prepsInsertProduct.executeQuery();
			while (resultSet.next()) {
				retorno.setIdPk(resultSet.getString(1));
				retorno.setStatus(resultSet.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
			InicioJob.gravarLog(e.getMessage() + e.getCause());
		} finally {
			try {
				if (prepsInsertProduct != null) {
					if (!prepsInsertProduct.isClosed()) {
						prepsInsertProduct.close();
					}
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retorno;
	}
	
	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public void cadastrarGalgoReserva(RunnerCatalog r, List<MarketCatalogue> proximaCorridaList, ResultadoStatusCorridaVO retornoIdPista) {
		ResultSet resultSet = null;
		PreparedStatement prepsInsertProduct = null;
		String update = "insert INTO public.\"Hist_galgo_betfair\" (nomegalgo,trap, id_hist_pista, iddogbetfair, reserva) VALUES"+
								"(?,?,?,?, ?)";
		Connection con = null;
		try {
			con = ConnectionFactory.getConection();
			prepsInsertProduct = con.prepareStatement(update);
			prepsInsertProduct.setString(1, r.getRunnerName().substring(3).replace("(res)", ""));
			prepsInsertProduct.setInt(2, Integer.valueOf(r.getRunnerName().substring(0,1)));
			prepsInsertProduct.setInt(3, Integer.valueOf(retornoIdPista.getIdPk()));
			prepsInsertProduct.setString(4, r.getSelectionId().toString());
			prepsInsertProduct.setBoolean(5, true);
			prepsInsertProduct.execute();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
			InicioJob.gravarLog(e.getMessage() + e.getCause());
		} finally {
			try {
				if (prepsInsertProduct != null && !prepsInsertProduct.isClosed()) {
						prepsInsertProduct.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
}
