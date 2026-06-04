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
import shared.exception.BusinessRuleException;
import shared.exception.InternalSystemException;
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;
import turno.dto.ConfirmacionTurnoDTO;
import turno.dto.ConfirmarTurnoRequest;
import turno.service.TurnoService;

@WebServlet("/cliente/turnos/confirmar")
public class ConfirmarTurnoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final TurnoService turnoService = AppContext.getTurnoService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			ConfirmarTurnoRequest confirmarTurnoRequest = buildRequest(request);
			ConfirmacionTurnoDTO confirmacion = turnoService.obtenerConfirmacionTurno(
					confirmarTurnoRequest.getIdNegocio(),
					confirmarTurnoRequest.getIdServicio(),
					confirmarTurnoRequest.getFecha(),
					confirmarTurnoRequest.getHora());

			request.setAttribute("confirmacion", confirmacion);
			ViewRenderer.forward(request, response, ViewPaths.CLIENTE_CONFIRMAR_TURNO);
		} catch (ValidationException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (ResourceNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		} catch (BusinessRuleException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
		} catch (InternalSystemException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			ConfirmarTurnoRequest confirmarTurnoRequest = buildRequest(request);
			HttpSession session = request.getSession(false);
			int idUsuario = UserSession.getUserId(session);

			turnoService.reservarTurno(
					confirmarTurnoRequest.getIdNegocio(),
					confirmarTurnoRequest.getIdServicio(),
					idUsuario,
					confirmarTurnoRequest.getFecha(),
					confirmarTurnoRequest.getHora());

			response.sendRedirect(request.getContextPath() + "/cliente/turnos");
		} catch (ValidationException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (ResourceNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		} catch (BusinessRuleException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
		} catch (InternalSystemException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private ConfirmarTurnoRequest buildRequest(HttpServletRequest request) {
		return new ConfirmarTurnoRequest(
				request.getParameter("idNegocio"),
				request.getParameter("idServicio"),
				request.getParameter("fecha"),
				request.getParameter("hora"));
	}
}
