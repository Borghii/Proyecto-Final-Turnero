package negocio.repository;

import negocio.entity.Negocio;
import shared.db.ConexionDB;
import shared.exception.InternalSystemException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	private static final String FIND_BY_USER_ID = """
			SELECT id_negocio, ciudad, nombre, id_usuario
			FROM negocios
			WHERE id_usuario = ?
			""";

	private static final String FIND_BY_ID = """
			SELECT id_negocio, ciudad, nombre, id_usuario
			FROM negocios
			WHERE id_negocio = ?
			""";

	private static final String UPDATE_NEGOCIO = """
			UPDATE negocios
			SET ciudad = ?, nombre = ?
			WHERE id_negocio = ? AND id_usuario = ?
			""";

	public JdbcNegocioRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}

	@Override
	public Negocio save(Negocio negocio) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement =
						connection.prepareStatement(INSERT_NEGOCIO, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, negocio.getCiudad());
			statement.setString(2, negocio.getNombre());
			statement.setInt(3, negocio.getIdUsuario());
			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return new Negocio(
							generatedKeys.getInt(1),
							negocio.getCiudad(),
							negocio.getNombre(),
							negocio.getIdUsuario());
				}
			}

			throw new InternalSystemException();
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public void update(Negocio negocio) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_NEGOCIO)) {

			statement.setString(1, negocio.getCiudad());
			statement.setString(2, negocio.getNombre());
			statement.setInt(3, negocio.getIdNegocio());
			statement.setInt(4, negocio.getIdUsuario());

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public List<Negocio> findAll() {
		List<Negocio> negocios = new ArrayList<>();

		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_ALL_NEGOCIOS);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				negocios.add(mapNegocio(resultSet));
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
		return negocios;
	}

	@Override
	public Optional<Negocio> findById(int idNegocio) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {

			statement.setInt(1, idNegocio);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(mapNegocio(resultSet));
				}
			}

			return Optional.empty();
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public Optional<Negocio> findByUserId(int userId) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_BY_USER_ID)) {

			statement.setInt(1, userId);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(mapNegocio(resultSet));
				}
			}

			return Optional.empty();
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	private Negocio mapNegocio(ResultSet resultSet) {
		try {
			return new Negocio(
					resultSet.getInt("id_negocio"),
					resultSet.getString("ciudad"),
					resultSet.getString("nombre"),
					resultSet.getInt("id_usuario"));
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}
}
