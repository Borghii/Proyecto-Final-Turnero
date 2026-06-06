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
import shared.exception.AuthorizationException;
import shared.exception.BusinessRuleException;
import shared.exception.InternalSystemException;
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
import shared.web.ViewRenderer;
import turno.dto.CancelarTurnoRequest;
import turno.service.TurnoService;

@WebServlet("/admin/turnos/cancelar")
public class CancelarTurnoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final TurnoService turnoService = AppContext.getTurnoService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			CancelarTurnoRequest cancelarTurnoRequest = new CancelarTurnoRequest(request.getParameter("idTurno"));

			HttpSession session = request.getSession(false);
			int idUsuario = UserSession.getUserId(session);

			turnoService.cancelarTurno(cancelarTurnoRequest.getIdTurno(), idUsuario);
			response.sendRedirect(request.getContextPath() + "/admin/home");
		} catch (ValidationException e) {
			ViewRenderer.redirectWithError(request, response, "/admin/home", e.getMessage());
		} catch (ResourceNotFoundException e) {
			ViewRenderer.redirectWithError(request, response, "/admin/home", e.getMessage());
		} catch (AuthorizationException e) {
			ViewRenderer.redirectWithError(request, response, "/admin/home", e.getMessage());
		} catch (BusinessRuleException e) {
			ViewRenderer.redirectWithError(request, response, "/admin/home", e.getMessage());
		} catch (InternalSystemException e) {
			ViewRenderer.redirectWithError(request, response, "/admin/home", e.getMessage());
		}
	}
}
