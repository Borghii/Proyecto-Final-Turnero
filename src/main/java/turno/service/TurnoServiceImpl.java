package turno.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import shared.enums.EstadoTurnoEnum;
import shared.exception.AuthorizationException;
import shared.exception.BusinessRuleException;
import shared.exception.ResourceNotFoundException;
import turno.dto.TurnoConServicio;
import turno.dto.TurnoParaCancelacion;
import turno.repository.TurnoRepository;

public class TurnoServiceImpl implements TurnoService {
	private final TurnoRepository turnoRepository;

	public TurnoServiceImpl(TurnoRepository turnoRepository) {
		this.turnoRepository = turnoRepository;
	}

	@Override
	public List<TurnoConServicio> findUpcomingByBusinessOwner(int idUsuario) throws SQLException {
		return turnoRepository.findByBusinessOwnerFromDateTime(idUsuario, LocalDate.now(), LocalTime.now());
	}

	@Override
	public void cancelarTurno(int idTurno, int idUsuario) throws SQLException {
		TurnoParaCancelacion turno = turnoRepository.findParaCancelacionById(idTurno)
				.orElseThrow(() -> new ResourceNotFoundException("El turno no existe"));

		if (turno.getIdUsuarioDuenioNegocio() != idUsuario) {
			throw new AuthorizationException("El turno no pertenece al usuario actual");
		}

		if (turno.getEstado() == EstadoTurnoEnum.CANCELADO) {
			throw new BusinessRuleException("El turno ya esta cancelado");
		}

		boolean cancelado = turnoRepository.cancelarPorDuenioDeNegocio(idTurno, idUsuario);
		if (!cancelado) {
			throw new BusinessRuleException("No se pudo cancelar el turno porque su estado cambio antes de guardar la cancelacion");
		}
	}
}
