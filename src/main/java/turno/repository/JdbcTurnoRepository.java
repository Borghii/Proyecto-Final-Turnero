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
import turno.dto.TurnoClienteDTO;
import turno.dto.TurnoConServicio;
import turno.dto.TurnoParaCancelacion;

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
			AND t.estado <> ?
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

	@Override
	public Optional<TurnoParaCancelacion> findParaCancelacionById(int idTurno) throws SQLException {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_FOR_CANCEL)) {

			statement.setInt(1, idTurno);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return Optional.empty();
				}

				return Optional.of(mapTurnoParaCancelacion(resultSet));
			}
		}
	}

	@Override
	public boolean cancelarPorDuenioDeNegocio(int idTurno, int idUsuario) throws SQLException {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_CANCEL_BY_BUSINESS_OWNER)) {

			statement.setString(1, EstadoTurnoEnum.CANCELADO.name());
			statement.setInt(2, idTurno);
			statement.setInt(3, idUsuario);
			statement.setString(4, EstadoTurnoEnum.CANCELADO.name());

			return statement.executeUpdate() > 0;
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
				EstadoTurnoEnum.valueOf(resultSet.getString("estado")));
	}

	private TurnoParaCancelacion mapTurnoParaCancelacion(ResultSet resultSet) throws SQLException {
		return new TurnoParaCancelacion(
				resultSet.getInt("id_turno"),
				resultSet.getInt("id_usuario_duenio_negocio"),
				EstadoTurnoEnum.valueOf(resultSet.getString("estado")));
	}
	
	private static final String SELECT_BY_CLIENTE = """
            SELECT t.id_turno, t.fecha, t.hora_inicio, t.hora_fin, t.estado, 
                   s.nombre AS nombre_servicio, s.precio, n.nombre AS nombre_negocio
            FROM turnos t
            JOIN servicios s ON t.id_servicio = s.id_servicio
            JOIN negocios n ON s.id_negocio = n.id_negocio
            WHERE t.id_usuario = ?
            ORDER BY t.fecha DESC, t.hora_inicio DESC
            """;

    @Override
    public List<TurnoClienteDTO> findByClienteId(int idUsuario) throws SQLException {
        List<TurnoClienteDTO> turnos = new ArrayList<>();
        
        try (Connection connection = conexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_CLIENTE)) {
            
            statement.setInt(1, idUsuario);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    turnos.add(new TurnoClienteDTO(
                        resultSet.getInt("id_turno"),
                        resultSet.getDate("fecha").toLocalDate(),
                        resultSet.getTime("hora_inicio").toLocalTime(),
                        resultSet.getTime("hora_fin").toLocalTime(),
                        resultSet.getString("nombre_negocio"),
                        resultSet.getString("nombre_servicio"),
                        resultSet.getBigDecimal("precio").doubleValue(),
                        EstadoTurnoEnum.valueOf(resultSet.getString("estado"))
                    ));
                }
            }
        }
        return turnos;
    }
}
