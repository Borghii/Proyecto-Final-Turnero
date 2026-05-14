package shared.context;

import auth.service.AuthService;
import negocio.repository.JdbcNegocioRepository;
import negocio.repository.NegocioRepository;
import negocio.service.NegocioService;
import servicio.repository.JdbcServicioRepository;
import servicio.repository.ServicioRepository;
import servicio.service.ServicioService;
import shared.db.ConexionDB;
import turno.repository.JdbcTurnoRepository;
import turno.repository.TurnoRepository;
import turno.service.TurnoService;
import usuario.repository.JdbcUsuarioRepository;
import usuario.repository.UsuarioRepository;
import usuario.service.UsuarioService;

public final class AppContext {
	private static final ConexionDB conexionDB = new ConexionDB(
			getEnv("DB_URL", "jdbc:mysql://localhost:3306/turnero?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"),
			getEnv("DB_USER", "root"),
			getEnv("DB_PASSWORD", ""));

	private static final UsuarioRepository usuarioRepository = new JdbcUsuarioRepository(conexionDB);
	private static final NegocioRepository negocioRepository = new JdbcNegocioRepository(conexionDB);
	private static final ServicioRepository servicioRepository = new JdbcServicioRepository(conexionDB);
	private static final TurnoRepository turnoRepository = new JdbcTurnoRepository(conexionDB);


	private static final UsuarioService usuarioService = new UsuarioService(usuarioRepository);
	private static final AuthService authService = new AuthService(usuarioService);
	private static final NegocioService negocioService = new NegocioService(negocioRepository);
	private static final ServicioService servicioService = new ServicioService(servicioRepository);
	private static final TurnoService turnoService = new TurnoService(
			turnoRepository,
			servicioRepository,
			usuarioRepository);

	private AppContext() {
	}

	private static String getEnv(String name, String defaultValue) {
		String value = System.getenv(name);
		return value == null || value.isBlank() ? defaultValue : value;
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
