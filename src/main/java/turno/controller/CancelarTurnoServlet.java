package turno.controller;

import java.io.IOException;
import java.sql.SQLException;

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
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
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
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (ResourceNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		} catch (AuthorizationException e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
		} catch (BusinessRuleException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo cancelar el turno");
		}
	}
}
