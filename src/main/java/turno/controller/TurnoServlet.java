package turno.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servicio.entity.Servicio;
import servicio.service.ServicioService;
import shared.context.AppContext;
import shared.exception.InternalSystemException;
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;
import turno.dto.SeleccionarNegocioRequest;

@WebServlet("/cliente/turnos/nuevo")
public class TurnoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ServicioService servicioService = AppContext.getServicioService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			SeleccionarNegocioRequest seleccionarNegocioRequest =
					new SeleccionarNegocioRequest(request.getParameter("idNegocio"));

			List<Servicio> servicios = servicioService.buscarPorNegocio(seleccionarNegocioRequest.getIdNegocio());

			request.setAttribute("servicios", servicios);
			request.setAttribute("idNegocio", seleccionarNegocioRequest.getIdNegocio());

			ViewRenderer.forward(request, response, ViewPaths.CLIENTE_SELECCIONAR_SERVICIO);
		} catch (ValidationException e) {
			redirectConErrorHome(request, response, e.getMessage());
		} catch (ResourceNotFoundException e) {
			redirectConErrorHome(request, response, e.getMessage());
		} catch (InternalSystemException e) {
			redirectConErrorHome(request, response, e.getMessage());
		}
	}

	private void redirectConErrorHome(HttpServletRequest request, HttpServletResponse response, String message)
			throws IOException {
		HttpSession session = request.getSession();
		session.setAttribute("error", message);
		response.sendRedirect(request.getContextPath() + "/cliente/home");
	}
}
