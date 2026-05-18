package turno.service;

import java.sql.SQLException;
import java.util.List;

import turno.dto.TurnoConServicio;

public interface TurnoService {
	List<TurnoConServicio> findUpcomingByBusinessOwner(int idUsuario) throws SQLException;
}
