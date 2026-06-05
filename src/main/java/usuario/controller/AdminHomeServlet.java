package usuario.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.session.UserSession;
import shared.context.AppContext;
import shared.exception.InternalSystemException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;
import turno.dto.TurnoConServicio;
import turno.service.TurnoService;

@WebServlet("/admin/home")
public class AdminHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final TurnoService turnoService = AppContext.getTurnoService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int idUsuario = UserSession.getUserId(session);
		cargarError(request, session);

		try {
			List<TurnoConServicio> turnos = turnoService.findUpcomingByBusinessOwner(idUsuario);
			request.setAttribute("turnos", turnos);
		} catch (InternalSystemException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.ADMIN_HOME, e.getMessage());
			return;
		}

		ViewRenderer.forward(request, response, ViewPaths.ADMIN_HOME);
	}

	private void cargarError(HttpServletRequest request, HttpSession session) {
		String error = (String) session.getAttribute("error");
		if (error != null) {
			request.setAttribute("error", error);
			session.removeAttribute("error");
		}
	}
}
