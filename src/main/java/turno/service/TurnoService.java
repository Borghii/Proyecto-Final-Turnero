package turno.service;

import java.util.List;

import turno.dto.TurnoClienteDTO;
import turno.dto.TurnoConServicio;

public interface TurnoService {
	List<TurnoConServicio> findUpcomingByBusinessOwner(int idUsuario);

	void cancelarTurno(int idTurno, int idUsuario);
	
	List<TurnoClienteDTO> obtenerTurnosPorCliente(int idUsuario);
}
