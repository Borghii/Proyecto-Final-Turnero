package turno.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import turno.dto.TurnoClienteDTO;
import turno.dto.TurnoConServicio;
import turno.dto.TurnoParaCancelacion;

public interface TurnoRepository {
	List<TurnoConServicio> findByBusinessOwnerFromDateTime(int idUsuario, LocalDate fromDate, LocalTime fromTime);

	Optional<TurnoParaCancelacion> findParaCancelacionById(int idTurno);

	boolean cancelarPorDuenioDeNegocio(int idTurno, int idUsuario);
	
	List<TurnoClienteDTO> findByClienteId(int idUsuario);
}
