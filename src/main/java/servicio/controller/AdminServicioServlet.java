package servicio.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.session.UserSession;
import negocio.entity.Negocio;
import negocio.service.NegocioService;
import servicio.entity.Servicio;
import servicio.service.ServicioService;
import shared.context.AppContext;
import shared.exception.AuthorizationException;
import shared.exception.BusinessRuleException;
import shared.exception.InternalSystemException;
import shared.exception.ResourceNotFoundException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;

@WebServlet("/admin/servicios")
public class AdminServicioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ServicioService servicioService = AppContext.getServicioService();
	private final NegocioService negocioService = AppContext.getNegocioService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int userId = UserSession.getUserId(session);

		cargarVista(request, response, userId);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession(false);
		int userId = UserSession.getUserId(session);

		try {
			Negocio negocio = negocioService.obtenerNegocioPorUsuario(userId);
			String accion = request.getParameter("accion");

			if ("guardar".equals(accion)) {
				Servicio nuevo = new Servicio(
						0,
						request.getParameter("nombre"),
						Double.parseDouble(request.getParameter("precio")),
						Integer.parseInt(request.getParameter("duracion")),
						negocio.getIdNegocio());
				servicioService.guardarServicio(nuevo);
			} else if ("editar".equals(accion)) {
				Servicio editado = new Servicio(
						Integer.parseInt(request.getParameter("idServicio")),
						request.getParameter("nombre"),
						Double.parseDouble(request.getParameter("precio")),
						Integer.parseInt(request.getParameter("duracion")),
						negocio.getIdNegocio());
				servicioService.actualizarServicio(editado);
			} else if ("eliminar".equals(accion)) {
				int idServicio = Integer.parseInt(request.getParameter("idServicio"));
				servicioService.eliminarServicio(idServicio, negocio.getIdNegocio());
			}

			response.sendRedirect(request.getContextPath() + "/admin/servicios");
		} catch (BusinessRuleException | AuthorizationException e) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			request.setAttribute("error", e.getMessage());
			cargarVista(request, response, userId);
		} catch (ResourceNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			request.setAttribute("error", e.getMessage());
			cargarVista(request, response, userId);
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			request.setAttribute("error", "Los datos ingresados no son validos.");
			cargarVista(request, response, userId);
		} catch (InternalSystemException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.ADMIN_SERVICIOS, e.getMessage());
		}
	}

	private void cargarVista(HttpServletRequest request, HttpServletResponse response, int userId)
			throws ServletException, IOException {
		try {
			Negocio negocio = negocioService.obtenerNegocioPorUsuario(userId);
			List<Servicio> servicios = servicioService.buscarPorNegocio(negocio.getIdNegocio());
			request.setAttribute("servicios", servicios);
			request.setAttribute("idNegocio", negocio.getIdNegocio());

			ViewRenderer.forward(request, response, ViewPaths.ADMIN_SERVICIOS);
		} catch (ResourceNotFoundException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_NOT_FOUND,
					ViewPaths.ADMIN_SERVICIOS, e.getMessage());
		} catch (InternalSystemException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.ADMIN_SERVICIOS, e.getMessage());
		}
	}
}
