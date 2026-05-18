package negocio.repository;

import negocio.entity.Negocio;
import shared.db.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	@Override
	public List<Negocio> findAll() throws SQLException {
		List<Negocio> negocios = new ArrayList<>();
		String sql = "SELECT id_negocio, ciudad, nombre, id_usuario FROM negocios";

		try (Connection connection = conexionDB.getConnection();
			 PreparedStatement statement = connection.prepareStatement(sql);
			 ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				// Acá llamamos al método de abajo para que arme el objeto
				negocios.add(mapNegocio(resultSet));
			}
		}
		return negocios;
	}

	// Este es el método que te faltaba. Toma los datos de MySQL y usa tu constructor
	private Negocio mapNegocio(ResultSet resultSet) throws SQLException {
		return new Negocio(
				resultSet.getInt("id_negocio"),
				resultSet.getString("ciudad"),
				resultSet.getString("nombre"),
				resultSet.getInt("id_usuario")
		);
	}
}
