package turno.controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shared.context.AppContext;
import shared.exception.BusinessRuleException;
import shared.exception.InternalSystemException;
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;
import turno.dto.CalendarioTurnoRequest;
import turno.service.TurnoService;

@WebServlet("/cliente/turnos/calendario")
public class CalendarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final TurnoService turnoService = AppContext.getTurnoService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			CalendarioTurnoRequest calendarioRequest = new CalendarioTurnoRequest(
					request.getParameter("idNegocio"),
					request.getParameter("idServicio"),
					request.getParameter("fecha"));

			List<LocalTime> horariosLibres = turnoService.obtenerHorariosDisponibles(
					calendarioRequest.getIdNegocio(),
					calendarioRequest.getIdServicio(),
					calendarioRequest.getFecha());

			request.setAttribute("horariosLibres", horariosLibres);
			request.setAttribute("idNegocio", calendarioRequest.getIdNegocio());
			request.setAttribute("idServicio", calendarioRequest.getIdServicio());
			request.setAttribute("fechaSeleccionada", calendarioRequest.getFecha());

			ViewRenderer.forward(request, response, ViewPaths.CLIENTE_CALENDARIO);
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
}
