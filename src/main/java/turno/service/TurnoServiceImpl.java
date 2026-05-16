package turno.service;

import servicio.repository.ServicioRepository;
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
}
