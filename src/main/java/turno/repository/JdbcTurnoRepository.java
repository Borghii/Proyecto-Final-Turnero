package turno.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import shared.db.ConexionDB;
import turno.dto.TurnoConServicio;

public class JdbcTurnoRepository implements TurnoRepository {
	private final ConexionDB conexionDB;

	private static final String SELECT_BY_BUSINESS_OWNER_FROM_DATE_TIME = """
			SELECT t.id_turno, t.id_servicio, s.nombre AS nombre_servicio,
				t.id_usuario, u.mail AS mail_cliente, u.nombre AS nombre_cliente,
				t.fecha, t.hora_inicio, t.hora_fin, t.estado
			FROM turnos t
			JOIN servicios s ON s.id_servicio = t.id_servicio
			JOIN negocios n ON n.id_negocio = s.id_negocio
			JOIN usuarios u ON u.id_usuario = t.id_usuario
			WHERE n.id_usuario = ?
			AND (
				t.fecha > ?
				OR (t.fecha = ? AND t.hora_inicio >= ?)
			)
			ORDER BY t.fecha, t.hora_inicio
			""";

	public JdbcTurnoRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}

	@Override
	public List<TurnoConServicio> findByBusinessOwnerFromDateTime(int idUsuario, LocalDate fromDate,
			LocalTime fromTime) throws SQLException {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_BUSINESS_OWNER_FROM_DATE_TIME)) {

			statement.setInt(1, idUsuario);
			statement.setDate(2, Date.valueOf(fromDate));
			statement.setDate(3, Date.valueOf(fromDate));
			statement.setTime(4, Time.valueOf(fromTime));

			try (ResultSet resultSet = statement.executeQuery()) {
				List<TurnoConServicio> turnos = new ArrayList<>();

				while (resultSet.next()) {
					turnos.add(mapTurno(resultSet));
				}

				return turnos;
			}
		}
	}

	private TurnoConServicio mapTurno(ResultSet resultSet) throws SQLException {
		return new TurnoConServicio(
				resultSet.getInt("id_turno"),
				resultSet.getInt("id_servicio"),
				resultSet.getString("nombre_servicio"),
				resultSet.getInt("id_usuario"),
				resultSet.getString("mail_cliente"),
				resultSet.getString("nombre_cliente"),
				resultSet.getDate("fecha").toLocalDate(),
				resultSet.getTime("hora_inicio").toLocalTime(),
				resultSet.getTime("hora_fin").toLocalTime(),
				resultSet.getString("estado"));
	}
}
