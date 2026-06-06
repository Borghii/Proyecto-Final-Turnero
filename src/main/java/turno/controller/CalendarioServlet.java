package turno.controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
			cargarError(request);

			ViewRenderer.forward(request, response, ViewPaths.CLIENTE_CALENDARIO);
		} catch (ValidationException e) {
			ViewRenderer.redirectWithError(request, response, "/cliente/home", e.getMessage());
		} catch (ResourceNotFoundException e) {
			ViewRenderer.redirectWithError(request, response, "/cliente/home", e.getMessage());
		} catch (BusinessRuleException e) {
			ViewRenderer.redirectWithError(request, response, "/cliente/home", e.getMessage());
		} catch (InternalSystemException e) {
			ViewRenderer.redirectWithError(request, response, "/cliente/home", e.getMessage());
		}
	}

	private void cargarError(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}

		String error = (String) session.getAttribute("error");
		if (error != null) {
			request.setAttribute("error", error);
			session.removeAttribute("error");
		}
	}

}
