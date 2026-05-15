package auth.session;

import javax.servlet.http.HttpSession;

import shared.enums.RolEnum;
import usuario.entity.Usuario;

public final class UserSession {
	private static final String USER_ID = "idUsuario";
	private static final String USER_EMAIL = "mailUsuario";
	private static final String USER_NAME = "nombreUsuario";
	private static final String USER_ROLE = "rolUsuario";

	private UserSession() {
	}

	public static void save(HttpSession session, Usuario user) {
		session.setAttribute(USER_ID, user.getIdUsuario());
		session.setAttribute(USER_EMAIL, user.getMail());
		session.setAttribute(USER_NAME, user.getNombre());
		session.setAttribute(USER_ROLE, user.getRole().name());
	}

	public static boolean isAuthenticated(HttpSession session) {
		return session != null && session.getAttribute(USER_ID) != null;
	}

	public static RolEnum getRole(HttpSession session) {
		if (session == null) {
			return null;
		}

		Object role = session.getAttribute(USER_ROLE);
		if (!(role instanceof String)) {
			return null;
		}

		try {
			return RolEnum.valueOf((String) role);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
