package turno.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.session.UserSession;
import shared.context.AppContext;
import shared.exception.InternalSystemException;
import shared.exception.ResourceNotFoundException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;
import turno.dto.ActividadMesDTO;
import turno.service.TurnoService;

@WebServlet("/admin/actividad")
public class ActividadMesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final TurnoService turnoService = AppContext.getTurnoService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int idUsuario = UserSession.getUserId(session);
		ViewRenderer.cargarError(request);

		try {
			ActividadMesDTO actividad = turnoService.obtenerActividadDelMesPorDuenio(idUsuario);
			request.setAttribute("actividad", actividad);
			ViewRenderer.forward(request, response, ViewPaths.ADMIN_ACTIVIDAD);
		} catch (ResourceNotFoundException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_NOT_FOUND,
					ViewPaths.ADMIN_ACTIVIDAD, e.getMessage());
		} catch (InternalSystemException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.ADMIN_ACTIVIDAD, e.getMessage());
		}
	}
}
