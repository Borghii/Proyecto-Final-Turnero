package servicio.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import servicio.entity.Servicio;
import shared.db.ConexionDB;
import shared.exception.InternalSystemException;

public class JdbcServicioRepository implements ServicioRepository {
	private final ConexionDB conexionDB;

	private static final String SELECT_BY_NEGOCIO = """
			SELECT id_servicio, nombre, precio, duracion, id_negocio
			FROM servicios
			WHERE id_negocio = ?
			ORDER BY nombre
			""";

	private static final String SELECT_BY_ID = """
			SELECT id_servicio, nombre, precio, duracion, id_negocio
			FROM servicios
			WHERE id_servicio = ?
			""";

	public JdbcServicioRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}

	@Override
	public List<Servicio> findByIdNegocio(int idNegocio) {
		List<Servicio> servicios = new ArrayList<>();

		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_NEGOCIO)) {

			statement.setInt(1, idNegocio);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					servicios.add(mapServicio(resultSet));
				}
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}

		return servicios;
	}

	@Override
	public Optional<Servicio> findById(int idServicio) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {

			statement.setInt(1, idServicio);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return Optional.empty();
				}

				return Optional.of(mapServicio(resultSet));
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	private Servicio mapServicio(ResultSet resultSet) {
		try {
			return new Servicio(
					resultSet.getInt("id_servicio"),
					resultSet.getString("nombre"),
					resultSet.getBigDecimal("precio").doubleValue(),
					resultSet.getInt("duracion"),
					resultSet.getInt("id_negocio"));
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}
}
