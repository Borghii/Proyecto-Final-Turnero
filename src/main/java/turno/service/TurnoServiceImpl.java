package turno.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import servicio.repository.ServicioRepository;
import turno.dto.TurnoConServicio;
import turno.repository.TurnoRepository;
import usuario.repository.UsuarioRepository;

public class TurnoServiceImpl implements TurnoService {
	private final TurnoRepository turnoRepository;
	private final ServicioRepository servicioRepository;
	private final UsuarioRepository usuarioRepository;

	public TurnoServiceImpl(TurnoRepository turnoRepository, ServicioRepository servicioRepository,
			UsuarioRepository usuarioRepository) {
		this.turnoRepository = turnoRepository;
		this.servicioRepository = servicioRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public List<TurnoConServicio> findUpcomingByBusinessOwner(int idUsuario) throws SQLException {
		return turnoRepository.findByBusinessOwnerFromDateTime(idUsuario, LocalDate.now(), LocalTime.now());
	}
}
