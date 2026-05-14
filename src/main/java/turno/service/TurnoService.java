package turno.service;

import servicio.repository.ServicioRepository;
import turno.repository.TurnoRepository;
import usuario.repository.UsuarioRepository;

public class TurnoService {
	private final TurnoRepository turnoRepository;
	private final ServicioRepository servicioRepository;
	private final UsuarioRepository usuarioRepository;

	public TurnoService(TurnoRepository turnoRepository, ServicioRepository servicioRepository,
			UsuarioRepository usuarioRepository) {
		this.turnoRepository = turnoRepository;
		this.servicioRepository = servicioRepository;
		this.usuarioRepository = usuarioRepository;
	}
}
