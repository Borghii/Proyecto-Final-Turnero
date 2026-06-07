package turno.controller;

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
import turno.dto.TurnoClienteDTO;
import turno.service.TurnoService;

@WebServlet("/cliente/turnos")
public class MisTurnosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final TurnoService turnoService = AppContext.getTurnoService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int idUsuario = UserSession.getUserId(session);
		ViewRenderer.cargarError(request);

		try {
			List<TurnoClienteDTO> misTurnos = turnoService.obtenerTurnosPorCliente(idUsuario);
			request.setAttribute("turnos", misTurnos);
			ViewRenderer.forward(request, response, ViewPaths.CLIENTE_MIS_TURNOS);
		} catch (InternalSystemException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.CLIENTE_HOME, e.getMessage());
		}
	}
}
