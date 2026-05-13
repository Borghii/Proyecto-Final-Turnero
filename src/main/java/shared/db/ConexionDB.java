package shared.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
	private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

	private final String url;
	private final String usuario;
	private final String password;

	public ConexionDB(String url, String usuario, String password) {
		cargarDriver();
		this.url = url;
		this.usuario = usuario;
		this.password = password;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, usuario, password);
	}

	private void cargarDriver() {
		try {
			Class.forName(MYSQL_DRIVER);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("No se encontro el driver JDBC de MySQL", e);
		}
	}
}
