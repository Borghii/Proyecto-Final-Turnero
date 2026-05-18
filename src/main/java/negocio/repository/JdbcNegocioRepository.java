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


	private static final String FIND_ALL_NEGOCIOS = """
			SELECT id_negocio, ciudad, nombre, id_usuario 
			FROM negocios
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

		try (Connection connection = conexionDB.getConnection();
			 PreparedStatement statement = connection.prepareStatement(FIND_ALL_NEGOCIOS);
			 ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				negocios.add(mapNegocio(resultSet));
			}
		}
		return negocios;
	}

	private Negocio mapNegocio(ResultSet resultSet) throws SQLException {
		return new Negocio(
				resultSet.getInt("id_negocio"),
				resultSet.getString("ciudad"),
				resultSet.getString("nombre"),
				resultSet.getInt("id_usuario")
		);
	}
}
