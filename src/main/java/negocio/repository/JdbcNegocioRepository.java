package negocio.repository;

import negocio.entity.Negocio;
import shared.db.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcNegocioRepository implements NegocioRepository {
	private final ConexionDB conexionDB;

	private static final String INSERT_NEGOCIO = """
			INSERT INTO negocios (ciudad, nombre, id_usuario) 
			VALUES (?, ?, ?)
			""";

	public JdbcNegocioRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}

	@Override
	public void save(Negocio negocio) throws SQLException {

		try(Connection connection = conexionDB.getConnection();
		    PreparedStatement statement = connection.prepareStatement(INSERT_NEGOCIO))
		{
			statement.setString(1, negocio.getCiudad());
			statement.setString(2, negocio.getNombre());
			statement.setInt(3, negocio.getIdUsuario());
			statement.executeUpdate();
		}

	}
}
