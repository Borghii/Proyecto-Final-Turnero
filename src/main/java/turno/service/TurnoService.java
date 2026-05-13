package turno.service;

import servicio.repository.ServicioRepository;
import turno.repository.TurnoRepository;
import turno.validator.TurnoValidator;
import usuario.repository.UsuarioRepository;

public class TurnoService {
	private final TurnoRepository turnoRepository;
	private final ServicioRepository servicioRepository;
	private final UsuarioRepository usuarioRepository;
	private final TurnoValidator turnoValidator;

	public TurnoService(TurnoRepository turnoRepository, ServicioRepository servicioRepository,
			UsuarioRepository usuarioRepository, TurnoValidator turnoValidator) {
		this.turnoRepository = turnoRepository;
		this.servicioRepository = servicioRepository;
		this.usuarioRepository = usuarioRepository;
		this.turnoValidator = turnoValidator;
	}
}
