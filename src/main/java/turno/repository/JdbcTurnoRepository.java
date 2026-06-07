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
import java.util.Optional;

import shared.db.ConexionDB;
import shared.enums.EstadoTurnoEnum;
import shared.exception.InternalSystemException;
import turno.dto.TurnoClienteDTO;
import turno.dto.TurnoConServicio;
import turno.dto.TurnoParaCancelacion;
import turno.entity.Turno;

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

	private static final String SELECT_FOR_CANCEL = """
			SELECT t.id_turno, n.id_usuario AS id_usuario_duenio_negocio, t.estado
			FROM turnos t
			JOIN servicios s ON s.id_servicio = t.id_servicio
			JOIN negocios n ON n.id_negocio = s.id_negocio
			WHERE t.id_turno = ?
			""";

	private static final String UPDATE_CANCEL_BY_BUSINESS_OWNER = """
			UPDATE turnos t
			JOIN servicios s ON s.id_servicio = t.id_servicio
			JOIN negocios n ON n.id_negocio = s.id_negocio
			SET t.estado = ?
			WHERE t.id_turno = ?
			AND n.id_usuario = ?
			AND t.estado != ?
			""";

	private static final String SELECT_BY_CLIENTE = """
			SELECT t.id_turno, t.fecha, t.hora_inicio, t.hora_fin, t.estado,
				s.nombre AS nombre_servicio, s.precio, n.nombre AS nombre_negocio
			FROM turnos t
			JOIN servicios s ON t.id_servicio = s.id_servicio
			JOIN negocios n ON s.id_negocio = n.id_negocio
			WHERE t.id_usuario = ?
			ORDER BY t.fecha DESC, t.hora_inicio DESC
			""";

	private static final String SELECT_BY_NEGOCIO_AND_FECHA = """
			SELECT t.id_turno, t.id_servicio, t.id_usuario, t.fecha, t.hora_inicio, t.hora_fin, t.estado
			FROM turnos t
			JOIN servicios s ON t.id_servicio = s.id_servicio
			WHERE s.id_negocio = ? AND t.fecha = ?
			""";

	private static final String SELECT_ACTIVOS_BY_NEGOCIO_FROM_DATE_TIME = """
			SELECT t.id_turno, t.id_servicio, t.id_usuario, t.fecha, t.hora_inicio, t.hora_fin, t.estado
			FROM turnos t
			JOIN servicios s ON t.id_servicio = s.id_servicio
			WHERE s.id_negocio = ?
			AND t.estado != ?
			AND (
				t.fecha > ?
				OR (t.fecha = ? AND t.hora_inicio >= ?)
			)
			ORDER BY t.fecha, t.hora_inicio
			""";

	private static final String INSERT_TURNO = """
			INSERT INTO turnos (id_servicio, id_usuario, fecha, hora_inicio, hora_fin, estado)
			VALUES (?, ?, ?, ?, ?, ?)
			""";

	public JdbcTurnoRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}

	@Override
	public List<TurnoConServicio> findByBusinessOwnerFromDateTime(int idUsuario, LocalDate fromDate,
			LocalTime fromTime) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_BUSINESS_OWNER_FROM_DATE_TIME)) {

			statement.setInt(1, idUsuario);
			statement.setDate(2, Date.valueOf(fromDate));
			statement.setDate(3, Date.valueOf(fromDate));
			statement.setTime(4, Time.valueOf(fromTime));

			try (ResultSet resultSet = statement.executeQuery()) {
				List<TurnoConServicio> turnos = new ArrayList<>();

				while (resultSet.next()) {
					turnos.add(mapTurnoConServicio(resultSet));
				}

				return turnos;
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public Optional<TurnoParaCancelacion> findParaCancelacionById(int idTurno) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_FOR_CANCEL)) {

			statement.setInt(1, idTurno);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return Optional.empty();
				}

				return Optional.of(mapTurnoParaCancelacion(resultSet));
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public boolean cancelarPorDuenioDeNegocio(int idTurno, int idUsuario) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_CANCEL_BY_BUSINESS_OWNER)) {

			statement.setString(1, EstadoTurnoEnum.CANCELADO.name());
			statement.setInt(2, idTurno);
			statement.setInt(3, idUsuario);
			statement.setString(4, EstadoTurnoEnum.CANCELADO.name());

			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public List<TurnoClienteDTO> findByClienteId(int idUsuario) {
		List<TurnoClienteDTO> turnos = new ArrayList<>();

		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_CLIENTE)) {

			statement.setInt(1, idUsuario);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					turnos.add(mapTurnoCliente(resultSet));
				}
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}

		return turnos;
	}

	@Override
	public List<Turno> findByNegocioAndFecha(int idNegocio, LocalDate fecha) {
		List<Turno> turnos = new ArrayList<>();

		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_NEGOCIO_AND_FECHA)) {

			statement.setInt(1, idNegocio);
			statement.setDate(2, Date.valueOf(fecha));

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					turnos.add(mapTurno(resultSet));
				}
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}

		return turnos;
	}

	@Override
	public List<Turno> findActivosByNegocioFromDateTime(int idNegocio, LocalDate fromDate, LocalTime fromTime) {
		List<Turno> turnos = new ArrayList<>();

		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ACTIVOS_BY_NEGOCIO_FROM_DATE_TIME)) {

			statement.setInt(1, idNegocio);
			statement.setString(2, EstadoTurnoEnum.CANCELADO.name());
			statement.setDate(3, Date.valueOf(fromDate));
			statement.setDate(4, Date.valueOf(fromDate));
			statement.setTime(5, Time.valueOf(fromTime));

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					turnos.add(mapTurno(resultSet));
				}
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}

		return turnos;
	}

	@Override
	public void save(Turno turno) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_TURNO)) {
			statement.setInt(1, turno.getIdServicio());
			statement.setInt(2, turno.getIdUsuario());
			statement.setDate(3, Date.valueOf(turno.getFecha()));
			statement.setTime(4, Time.valueOf(turno.getHoraInicio()));
			statement.setTime(5, Time.valueOf(turno.getHoraFin()));
			statement.setString(6, turno.getEstado().name());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	private TurnoConServicio mapTurnoConServicio(ResultSet resultSet) {
		try {
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
					EstadoTurnoEnum.valueOf(resultSet.getString("estado")));
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	private TurnoParaCancelacion mapTurnoParaCancelacion(ResultSet resultSet) {
		try {
			return new TurnoParaCancelacion(
					resultSet.getInt("id_turno"),
					resultSet.getInt("id_usuario_duenio_negocio"),
					EstadoTurnoEnum.valueOf(resultSet.getString("estado")));
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	private TurnoClienteDTO mapTurnoCliente(ResultSet resultSet) {
		try {
			return new TurnoClienteDTO(
					resultSet.getInt("id_turno"),
					resultSet.getDate("fecha").toLocalDate(),
					resultSet.getTime("hora_inicio").toLocalTime(),
					resultSet.getTime("hora_fin").toLocalTime(),
					resultSet.getString("nombre_negocio"),
					resultSet.getString("nombre_servicio"),
					resultSet.getBigDecimal("precio").doubleValue(),
					EstadoTurnoEnum.valueOf(resultSet.getString("estado")));
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	private Turno mapTurno(ResultSet resultSet) {
		try {
			return new Turno(
					resultSet.getInt("id_turno"),
					resultSet.getInt("id_servicio"),
					resultSet.getInt("id_usuario"),
					resultSet.getDate("fecha").toLocalDate(),
					resultSet.getTime("hora_inicio").toLocalTime(),
					resultSet.getTime("hora_fin").toLocalTime(),
					EstadoTurnoEnum.valueOf(resultSet.getString("estado")));
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}
}
