package turno.repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import turno.dto.TurnoConServicio;

public interface TurnoRepository {
	List<TurnoConServicio> findByBusinessOwnerFromDateTime(int idUsuario, LocalDate fromDate, LocalTime fromTime)
			throws SQLException;
}
