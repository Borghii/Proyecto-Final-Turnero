package turno.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import shared.enums.EstadoTurnoEnum;
import shared.exception.AuthorizationException;
import shared.exception.BusinessRuleException;
import shared.exception.ResourceNotFoundException;
import turno.dto.TurnoClienteDTO;
import turno.dto.TurnoConServicio;
import turno.dto.TurnoParaCancelacion;
import turno.repository.TurnoRepository;

public class TurnoServiceImpl implements TurnoService {
	private final TurnoRepository turnoRepository;

	public TurnoServiceImpl(TurnoRepository turnoRepository) {
		this.turnoRepository = turnoRepository;
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
    public List<TurnoClienteDTO> obtenerTurnosPorCliente(int idUsuario) {
        return turnoRepository.findByClienteId(idUsuario);
    }
}
