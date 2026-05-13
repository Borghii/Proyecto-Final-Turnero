package shared.context;

import auth.service.AuthService;
import negocio.repository.NegocioRepository;
import negocio.service.NegocioService;
import servicio.repository.ServicioRepository;
import servicio.service.ServicioService;
import shared.db.ConexionDB;
import turno.repository.TurnoRepository;
import turno.service.TurnoService;
import turno.validator.TurnoValidator;
import usuario.repository.UsuarioRepository;
import usuario.service.UsuarioService;

public final class AppContext {
	private static final ConexionDB conexionDB = new ConexionDB(
			"jdbc:mysql://localhost:3306/test",
			"root",
			"");

	private static final UsuarioRepository usuarioRepository = new UsuarioRepository(conexionDB);
	private static final NegocioRepository negocioRepository = new NegocioRepository(conexionDB);
	private static final ServicioRepository servicioRepository = new ServicioRepository(conexionDB);
	private static final TurnoRepository turnoRepository = new TurnoRepository(conexionDB);

	private static final TurnoValidator turnoValidator = new TurnoValidator();

	private static final AuthService authService = new AuthService(usuarioRepository);
	private static final UsuarioService usuarioService = new UsuarioService(usuarioRepository);
	private static final NegocioService negocioService = new NegocioService(negocioRepository);
	private static final ServicioService servicioService = new ServicioService(servicioRepository);
	private static final TurnoService turnoService = new TurnoService(
			turnoRepository,
			servicioRepository,
			usuarioRepository,
			turnoValidator);

	private AppContext() {
	}

	public static AuthService getAuthService() {
		return authService;
	}

	public static UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public static NegocioService getNegocioService() {
		return negocioService;
	}

	public static ServicioService getServicioService() {
		return servicioService;
	}

	public static TurnoService getTurnoService() {
		return turnoService;
	}
}
