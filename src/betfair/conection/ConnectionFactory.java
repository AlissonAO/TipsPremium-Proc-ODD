package betfair.conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class ConnectionFactory {

	private static ConnectionFactory instacia = null;

	

	public static synchronized ConnectionFactory getInstance() {
		if (instacia == null)
			instacia = new ConnectionFactory();
		return instacia;
	}

	public static Connection getConection() throws SQLException {
		Connection conexao = null;
		// String url =
		// "jdbc:postgresql://tips.cmgh3qfxzq5c.sa-east-1.rds.amazonaws.com:5432/tips";
		// // Nome da
		String url = "jdbc:postgresql://tips.chgdctshl9nz.sa-east-1.rds.amazonaws.com:5432/tips"; //homol
//		String url = "jdbc:postgresql://tips.cqm9b7amos8q.sa-east-1.rds.amazonaws.com:5432/tips"; // prod
		String user = "tips"; // nome do usuário do MySQL
		String password = "alisson123"; // senha do MySQL
		conexao = DriverManager.getConnection(url, user, password);
		return conexao;
	}

}