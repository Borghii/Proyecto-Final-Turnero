package servicio.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servicio.dto.BuscarServiciosRequest;
import servicio.entity.Servicio;
import servicio.service.ServicioService;
import shared.context.AppContext;
import shared.exception.InternalSystemException;
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;

@WebServlet("/cliente/servicios")
public class ServicioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ServicioService servicioService = AppContext.getServicioService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			BuscarServiciosRequest buscarServiciosRequest =
					new BuscarServiciosRequest(request.getParameter("idNegocio"));

			List<Servicio> servicios = servicioService.buscarPorNegocio(buscarServiciosRequest.getIdNegocio());

			request.setAttribute("servicios", servicios);
			request.setAttribute("idNegocio", buscarServiciosRequest.getIdNegocio());

			ViewRenderer.forward(request, response, ViewPaths.CLIENTE_SELECCIONAR_SERVICIO);
		} catch (ValidationException e) {
			ViewRenderer.redirectWithError(request, response, "/cliente/home", e.getMessage());
		} catch (ResourceNotFoundException e) {
			ViewRenderer.redirectWithError(request, response, "/cliente/home", e.getMessage());
		} catch (InternalSystemException e) {
			ViewRenderer.redirectWithError(request, response, "/cliente/home", e.getMessage());
		}
	}
}
