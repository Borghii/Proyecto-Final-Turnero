package turno.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import negocio.entity.Horario;
import negocio.repository.HorarioRepository;
import servicio.entity.Servicio;
import servicio.repository.ServicioRepository;
import shared.enums.EstadoTurnoEnum;
import shared.exception.AuthorizationException;
import shared.exception.BusinessRuleException;
import shared.exception.ResourceNotFoundException;
import turno.dto.ConfirmacionTurnoDTO;
import turno.dto.TurnoClienteDTO;
import turno.dto.TurnoConServicio;
import turno.dto.TurnoParaCancelacion;
import turno.entity.Turno;
import turno.repository.TurnoRepository;

public class TurnoServiceImpl implements TurnoService {
	private static final int SALTO_MINUTOS = 30;

	private final TurnoRepository turnoRepository;
	private final ServicioRepository servicioRepository;
	private final HorarioRepository horarioRepository;

	public TurnoServiceImpl(TurnoRepository turnoRepository, ServicioRepository servicioRepository,
			HorarioRepository horarioRepository) {
		this.turnoRepository = turnoRepository;
		this.servicioRepository = servicioRepository;
		this.horarioRepository = horarioRepository;
	}

	@Override
	public List<TurnoConServicio> findUpcomingByBusinessOwner(int idUsuario) {
		return turnoRepository.findByBusinessOwnerFromDateTime(idUsuario, LocalDate.now(), LocalTime.now());
	}

	@Override
	public void cancelarTurno(int idTurno, int idUsuario) {
		TurnoParaCancelacion turno = turnoRepository.findParaCancelacionById(idTurno)
				.orElseThrow(() -> new ResourceNotFoundException("El turno no existe"));

		if (turno.getIdUsuarioDuenioNegocio() != idUsuario) {
			throw new AuthorizationException("No tenes permiso para cancelar este turno");
		}

		if (turno.getEstado() == EstadoTurnoEnum.CANCELADO) {
			throw new BusinessRuleException("El turno ya esta cancelado");
		}

		boolean cancelado = turnoRepository.cancelarPorDuenioDeNegocio(idTurno, idUsuario);
		if (!cancelado) {
			throw new BusinessRuleException("No se pudo cancelar el turno. Intenta nuevamente.");
		}
	}

	@Override
	public List<LocalTime> obtenerHorariosDisponibles(int idNegocio, int idServicio, LocalDate fecha) {
		Servicio servicio = obtenerServicioDelNegocio(idNegocio, idServicio);
		return calcularHorariosDisponibles(idNegocio, servicio, fecha);
	}

	@Override
	public ConfirmacionTurnoDTO obtenerConfirmacionTurno(int idNegocio, int idServicio, LocalDate fecha,
			LocalTime horaInicio) {
		Servicio servicio = obtenerServicioDelNegocio(idNegocio, idServicio);
		validarHorarioDisponible(idNegocio, servicio, fecha, horaInicio);

		return new ConfirmacionTurnoDTO(
				idNegocio,
				servicio.getIdServicio(),
				servicio.getNombre(),
				servicio.getPrecio(),
				servicio.getDuracion(),
				fecha,
				horaInicio,
				horaInicio.plusMinutes(servicio.getDuracion()));
	}

	@Override
	public void reservarTurno(int idNegocio, int idServicio, int idUsuario, LocalDate fecha, LocalTime horaInicio) {
		Servicio servicio = obtenerServicioDelNegocio(idNegocio, idServicio);
		validarHorarioDisponible(idNegocio, servicio, fecha, horaInicio);

		Turno turno = new Turno(
				0,
				servicio.getIdServicio(),
				idUsuario,
				fecha,
				horaInicio,
				horaInicio.plusMinutes(servicio.getDuracion()),
				EstadoTurnoEnum.CONFIRMADO);

		turnoRepository.save(turno);
	}

	@Override
	public List<TurnoClienteDTO> obtenerTurnosPorCliente(int idUsuario) {
		return turnoRepository.findByClienteId(idUsuario);
	}

	private Servicio obtenerServicioDelNegocio(int idNegocio, int idServicio) {
		Servicio servicio = servicioRepository.findById(idServicio)
				.orElseThrow(() -> new ResourceNotFoundException("El servicio seleccionado no existe"));

		if (servicio.getIdNegocio() != idNegocio) {
			throw new BusinessRuleException("El servicio seleccionado no pertenece al negocio indicado");
		}

		if (servicio.getDuracion() <= 0) {
			throw new BusinessRuleException("El servicio seleccionado no tiene una duracion valida");
		}

		return servicio;
	}

	private List<LocalTime> calcularHorariosDisponibles(int idNegocio, Servicio servicio, LocalDate fecha) {
		List<Turno> turnosDelDia = turnoRepository.findByNegocioAndFecha(idNegocio, fecha);
		List<LocalTime> horariosDisponibles = new ArrayList<>();

		Horario horario = obtenerHorarioDelDia(idNegocio, fecha);
		if (horario == null || !horario.isActivo()) {
			return horariosDisponibles;
		}

		agregarHorariosDelBloque(horariosDisponibles, turnosDelDia, servicio, fecha,
				horario.getMananaDesde(), horario.getMananaHasta());
		agregarHorariosDelBloque(horariosDisponibles, turnosDelDia, servicio, fecha,
				horario.getTardeDesde(), horario.getTardeHasta());

		return horariosDisponibles;
	}

	private Horario obtenerHorarioDelDia(int idNegocio, LocalDate fecha) {
		int dia = fecha.getDayOfWeek().getValue();
		List<Horario> horarios = horarioRepository.findByNegocioId(idNegocio);

		for (Horario horario : horarios) {
			if (horario.getDia() == dia) {
				return horario;
			}
		}

		return null;
	}

	private void agregarHorariosDelBloque(List<LocalTime> horariosDisponibles, List<Turno> turnosDelDia,
			Servicio servicio, LocalDate fecha, LocalTime horaDesde, LocalTime horaHasta) {
		if (horaDesde == null || horaHasta == null) {
			return;
		}

		LocalTime horaActual = horaDesde;
		while (!horaActual.plusMinutes(servicio.getDuracion()).isAfter(horaHasta)) {
			if (esHorarioReservable(fecha, horaActual) && !estaOcupado(turnosDelDia, horaActual)) {
				horariosDisponibles.add(horaActual);
			}

			horaActual = horaActual.plusMinutes(SALTO_MINUTOS);
		}
	}

	private void validarHorarioDisponible(int idNegocio, Servicio servicio, LocalDate fecha, LocalTime horaInicio) {
		if (!calcularHorariosDisponibles(idNegocio, servicio, fecha).contains(horaInicio)) {
			throw new BusinessRuleException("El horario seleccionado ya no esta disponible");
		}
	}

	private boolean estaOcupado(List<Turno> turnosDelDia, LocalTime horaInicio) {
		for (Turno turno : turnosDelDia) {
			if (turno.getEstado() == EstadoTurnoEnum.CONFIRMADO && turno.getHoraInicio().equals(horaInicio)) {
				return true;
			}
		}

		return false;
	}

	private boolean esHorarioReservable(LocalDate fecha, LocalTime horaInicio) {
		return !fecha.equals(LocalDate.now()) || !horaInicio.isBefore(LocalTime.now());
	}
}
