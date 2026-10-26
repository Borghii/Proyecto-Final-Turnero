package negocio.repository;

import negocio.entity.Horario;
import shared.db.ConexionDB;
import shared.exception.InternalSystemException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcHorarioRepository implements HorarioRepository {
	private final ConexionDB conexionDB;

	private static final String INSERT_HORARIO = """
			INSERT INTO horarios (id_negocio, dia, manana_desde, manana_hasta, tarde_desde, tarde_hasta, activo)
			VALUES (?, ?, ?, ?, ?, ?, ?)
			""";

	private static final String UPDATE_HORARIO = """
			UPDATE horarios
			SET manana_desde = ?, manana_hasta = ?, tarde_desde = ?, tarde_hasta = ?, activo = ?
			WHERE id_negocio = ? AND dia = ?
			""";

	private static final String FIND_BY_NEGOCIO_ID = """
			SELECT dia, manana_desde, manana_hasta, tarde_desde, tarde_hasta, activo
			FROM horarios
			WHERE id_negocio = ?
			ORDER BY dia
			""";

	public JdbcHorarioRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}

	@Override
	public void saveAll(int idNegocio, List<Horario> horarios) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_HORARIO)) {

			for (Horario horario : horarios) {
				statement.setInt(1, idNegocio);
				statement.setInt(2, horario.getDia());
				setNullableTime(statement, 3, horario.getMananaDesde());
				setNullableTime(statement, 4, horario.getMananaHasta());
				setNullableTime(statement, 5, horario.getTardeDesde());
				setNullableTime(statement, 6, horario.getTardeHasta());
				statement.setBoolean(7, horario.isActivo());
				statement.addBatch();
			}

			statement.executeBatch();
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public void updateAll(int idNegocio, List<Horario> horarios) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_HORARIO)) {

			for (Horario horario : horarios) {
				setNullableTime(statement, 1, horario.getMananaDesde());
				setNullableTime(statement, 2, horario.getMananaHasta());
				setNullableTime(statement, 3, horario.getTardeDesde());
				setNullableTime(statement, 4, horario.getTardeHasta());
				statement.setBoolean(5, horario.isActivo());
				statement.setInt(6, idNegocio);
				statement.setInt(7, horario.getDia());
				statement.addBatch();
			}

			statement.executeBatch();
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public List<Horario> findByNegocioId(int idNegocio) {
		List<Horario> horarios = new ArrayList<>();

		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_BY_NEGOCIO_ID)) {

			statement.setInt(1, idNegocio);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					horarios.add(mapHorario(resultSet));
				}
			}

			return horarios;
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	private Horario mapHorario(ResultSet resultSet) {
		try {
			Time mananaDesde = resultSet.getTime("manana_desde");
			Time mananaHasta = resultSet.getTime("manana_hasta");
			Time tardeDesde = resultSet.getTime("tarde_desde");
			Time tardeHasta = resultSet.getTime("tarde_hasta");

			return new Horario(
					resultSet.getInt("dia"),
					mananaDesde != null ? mananaDesde.toLocalTime() : null,
					mananaHasta != null ? mananaHasta.toLocalTime() : null,
					tardeDesde != null ? tardeDesde.toLocalTime() : null,
					tardeHasta != null ? tardeHasta.toLocalTime() : null,
					resultSet.getBoolean("activo"));
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	private void setNullableTime(PreparedStatement statement, int parameterIndex, LocalTime time) throws SQLException {
		if (time == null) {
			statement.setNull(parameterIndex, Types.TIME);
			return;
		}

		statement.setTime(parameterIndex, Time.valueOf(time));
	}
}
