package turno.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import turno.dto.ActividadMesDTO;
import turno.dto.ConfirmacionTurnoDTO;
import turno.dto.TurnoClienteDTO;
import turno.dto.TurnoConServicio;

public interface TurnoService {
	List<TurnoConServicio> findUpcomingByBusinessOwner(int idUsuario);

	void cancelarTurno(int idTurno, int idUsuario);

	void cancelarTurnoPorCliente(int idTurno, int idUsuario);
	
	List<LocalTime> obtenerHorariosDisponibles(int idNegocio, int idServicio, LocalDate fecha);

	ConfirmacionTurnoDTO obtenerConfirmacionTurno(int idNegocio, int idServicio, LocalDate fecha, LocalTime horaInicio);

	void reservarTurno(int idNegocio, int idServicio, int idUsuario, LocalDate fecha, LocalTime horaInicio);

	List<TurnoClienteDTO> obtenerTurnosPorCliente(int idUsuario);

	ActividadMesDTO obtenerActividadDelMesPorDuenio(int idUsuario);
}
