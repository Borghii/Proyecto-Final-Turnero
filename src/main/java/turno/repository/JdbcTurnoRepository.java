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
import java.util.logging.Level;
import java.util.logging.Logger;

import shared.db.ConexionDB;
import shared.enums.EstadoTurnoEnum;
import shared.exception.BusinessRuleException;
import shared.exception.InternalSystemException;
import turno.dto.ActividadMesDTO;
import turno.dto.TurnoClienteDTO;
import turno.dto.TurnoConServicio;
import turno.dto.TurnoParaCancelacion;
import turno.entity.Turno;

public class JdbcTurnoRepository implements TurnoRepository {
	private static final Logger LOGGER = Logger.getLogger(JdbcTurnoRepository.class.getName());
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
			SELECT t.id_turno, t.id_usuario AS id_usuario_cliente,
				n.id_usuario AS id_usuario_duenio_negocio,
				t.fecha, t.hora_inicio, t.estado
			FROM turnos t
			JOIN servicios s ON s.id_servicio = t.id_servicio
			JOIN negocios n ON n.id_negocio = s.id_negocio
			WHERE t.id_turno = ?
			""";

	private static final String UPDATE_CANCEL_BY_BUSINESS_OWNER = """
			UPDATE turnos t
			JOIN servicios s ON s.id_servicio = t.id_servicio
			JOIN negocios n ON n.id_negocio = s.id_negocio
			SET t.estado = 'CANCELADO'
			WHERE t.id_turno = ?
			AND n.id_usuario = ?
			""";

	private static final String UPDATE_CANCEL_BY_CLIENT = """
			UPDATE turnos
			SET estado = 'CANCELADO'
			WHERE id_turno = ?
			AND id_usuario = ?
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

	private static final String SELECT_ACTIVE_BY_CLIENTE_AND_FECHA = """
			SELECT id_turno, id_servicio, id_usuario, fecha, hora_inicio, hora_fin, estado
			FROM turnos
			WHERE id_usuario = ?
			AND fecha = ?
			AND estado != 'CANCELADO'
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
			AND t.estado != 'CANCELADO'
			AND (
				t.fecha > ?
				OR (t.fecha = ? AND t.hora_inicio >= ?)
			)
			ORDER BY t.fecha, t.hora_inicio
			""";

	private static final String SELECT_ACTIVIDAD_MES_BY_BUSINESS_OWNER = """
			SELECT
				COUNT(t.id_turno) AS total_turnos,
				SUM(CASE WHEN t.estado = 'CANCELADO' THEN 1 ELSE 0 END) AS turnos_cancelados,
				SUM(CASE
					WHEN t.estado != 'CANCELADO'
					AND (
						t.fecha < CURRENT_DATE()
						OR (t.fecha = CURRENT_DATE() AND t.hora_fin <= CURRENT_TIME())
					)
					THEN 1 ELSE 0
				END) AS turnos_realizados,
				SUM(CASE
					WHEN t.estado != 'CANCELADO'
					AND (
						t.fecha < CURRENT_DATE()
						OR (t.fecha = CURRENT_DATE() AND t.hora_fin <= CURRENT_TIME())
					)
					THEN s.precio ELSE 0
				END) AS ingresos
			FROM negocios n
			LEFT JOIN servicios s ON s.id_negocio = n.id_negocio
			LEFT JOIN turnos t ON t.id_servicio = s.id_servicio
				AND MONTH(t.fecha) = MONTH(CURRENT_DATE())
				AND YEAR(t.fecha) = YEAR(CURRENT_DATE())
			WHERE n.id_usuario = ?
			GROUP BY n.id_negocio
			""";

	private static final String INSERT_TURNO = """
			INSERT INTO turnos (id_servicio, id_usuario, fecha, hora_inicio, hora_fin, estado)
			VALUES (?, ?, ?, ?, ?, ?)
			""";

	private static final String SELECT_NEGOCIO_BY_SERVICIO = """
			SELECT id_negocio
			FROM servicios
			WHERE id_servicio = ?
			""";

	private static final String LOCK_NEGOCIO = """
			SELECT id_negocio
			FROM negocios
			WHERE id_negocio = ?
			FOR UPDATE
			""";

	private static final String LOCK_USUARIO = """
			SELECT id_usuario
			FROM usuarios
			WHERE id_usuario = ?
			FOR UPDATE
			""";

	private static final String EXISTS_OVERLAP_BY_NEGOCIO = """
			SELECT 1
			FROM turnos t
			JOIN servicios s ON s.id_servicio = t.id_servicio
			WHERE s.id_negocio = ?
			AND t.fecha = ?
			AND t.estado != 'CANCELADO'
			AND t.hora_inicio < ?
			AND t.hora_fin > ?
			LIMIT 1
			""";

	private static final String EXISTS_OVERLAP_BY_CLIENTE = """
			SELECT 1
			FROM turnos
			WHERE id_usuario = ?
			AND fecha = ?
			AND estado != 'CANCELADO'
			AND hora_inicio < ?
			AND hora_fin > ?
			LIMIT 1
			""";

	private static final String EXISTS_BY_SERVICIO_ID = """
			SELECT 1
			FROM turnos
			WHERE id_servicio = ?
			LIMIT 1
			""";

	private static final String EXISTS_ACTIVOS_BY_SERVICIO_FROM_DATE_TIME = """
			SELECT 1
			FROM turnos
			WHERE id_servicio = ?
			AND estado != 'CANCELADO'
			AND (
				fecha > ?
				OR (fecha = ? AND hora_inicio >= ?)
			)
			LIMIT 1
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
	public void cancelarPorDuenioDeNegocio(int idTurno, int idUsuario) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_CANCEL_BY_BUSINESS_OWNER)) {

			statement.setInt(1, idTurno);
			statement.setInt(2, idUsuario);

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public void cancelarPorCliente(int idTurno, int idUsuario) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_CANCEL_BY_CLIENT)) {

			statement.setInt(1, idTurno);
			statement.setInt(2, idUsuario);

			statement.executeUpdate();
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
	public List<Turno> findActivosByClienteAndFecha(int idUsuario, LocalDate fecha) {
		List<Turno> turnos = new ArrayList<>();

		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ACTIVE_BY_CLIENTE_AND_FECHA)) {

			statement.setInt(1, idUsuario);
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
			statement.setDate(2, Date.valueOf(fromDate));
			statement.setDate(3, Date.valueOf(fromDate));
			statement.setTime(4, Time.valueOf(fromTime));

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
	public Optional<ActividadMesDTO> findActividadMesByBusinessOwner(int idUsuario) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ACTIVIDAD_MES_BY_BUSINESS_OWNER)) {

			statement.setInt(1, idUsuario);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return Optional.empty();
				}

				return Optional.of(mapActividadMes(resultSet));
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public void save(Turno turno) {
		try (Connection connection = conexionDB.getConnection()) {
			connection.setAutoCommit(false);

			try {
				int idNegocio = findNegocioIdByServicio(connection, turno.getIdServicio());
				lockRegistro(connection, LOCK_NEGOCIO, idNegocio);
				lockRegistro(connection, LOCK_USUARIO, turno.getIdUsuario());

				if (existsOverlap(connection, EXISTS_OVERLAP_BY_NEGOCIO, idNegocio, turno)) {
					throw new BusinessRuleException("El horario seleccionado ya no esta disponible");
				}

				if (existsOverlap(connection, EXISTS_OVERLAP_BY_CLIENTE, turno.getIdUsuario(), turno)) {
					throw new BusinessRuleException("Ya tenes un turno reservado que se superpone con ese horario");
				}

				insertTurno(connection, turno);
				connection.commit();
			} catch (BusinessRuleException e) {
				rollback(connection);
				throw e;
			} catch (SQLException e) {
				rollback(connection);

				if (e.getErrorCode() == 1062) {
					throw new BusinessRuleException("El horario seleccionado ya no esta disponible");
				}

				LOGGER.log(Level.SEVERE, "No se pudo guardar el turno", e);
				throw new InternalSystemException(e);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "No se pudo guardar el turno", e);
			throw new InternalSystemException(e);
		}
	}

	private int findNegocioIdByServicio(Connection connection, int idServicio) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(SELECT_NEGOCIO_BY_SERVICIO)) {
			statement.setInt(1, idServicio);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					throw new BusinessRuleException("El servicio seleccionado no existe");
				}

				return resultSet.getInt("id_negocio");
			}
		}
	}

	private void lockRegistro(Connection connection, String sql, int id) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			statement.executeQuery();
		}
	}

	private boolean existsOverlap(Connection connection, String sql, int id, Turno turno) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			statement.setDate(2, Date.valueOf(turno.getFecha()));
			statement.setTime(3, Time.valueOf(turno.getHoraFin()));
			statement.setTime(4, Time.valueOf(turno.getHoraInicio()));

			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
		}
	}

	private void insertTurno(Connection connection, Turno turno) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(INSERT_TURNO)) {
			statement.setInt(1, turno.getIdServicio());
			statement.setInt(2, turno.getIdUsuario());
			statement.setDate(3, Date.valueOf(turno.getFecha()));
			statement.setTime(4, Time.valueOf(turno.getHoraInicio()));
			statement.setTime(5, Time.valueOf(turno.getHoraFin()));
			statement.setString(6, turno.getEstado().name());
			statement.executeUpdate();
		}
	}

	private void rollback(Connection connection) {
		try {
			connection.rollback();
		} catch (SQLException rollbackError) {
			LOGGER.log(Level.SEVERE, "No se pudo revertir la reserva", rollbackError);
		}
	}

	@Override
	public boolean existsByServicioId(int idServicio) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(EXISTS_BY_SERVICIO_ID)) {
			statement.setInt(1, idServicio);

			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public boolean existsActivosByServicioFromDateTime(int idServicio, LocalDate fromDate, LocalTime fromTime) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(EXISTS_ACTIVOS_BY_SERVICIO_FROM_DATE_TIME)) {
			statement.setInt(1, idServicio);
			statement.setDate(2, Date.valueOf(fromDate));
			statement.setDate(3, Date.valueOf(fromDate));
			statement.setTime(4, Time.valueOf(fromTime));

			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
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
					resultSet.getInt("id_usuario_cliente"),
					resultSet.getInt("id_usuario_duenio_negocio"),
					resultSet.getDate("fecha").toLocalDate(),
					resultSet.getTime("hora_inicio").toLocalTime(),
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

	private ActividadMesDTO mapActividadMes(ResultSet resultSet) {
		try {
			return new ActividadMesDTO(
					resultSet.getInt("total_turnos"),
					resultSet.getInt("turnos_realizados"),
					resultSet.getInt("turnos_cancelados"),
					resultSet.getDouble("ingresos"));
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}
}
