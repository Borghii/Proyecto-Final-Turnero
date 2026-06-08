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
import servicio.dto.GuardarServicioRequest;
import servicio.entity.Servicio;
import servicio.service.ServicioService;
import shared.context.AppContext;
import shared.exception.BusinessRuleException;
import shared.exception.InternalSystemException;
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;

@WebServlet("/admin/servicios")
public class AdminServiciosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ServicioService servicioService = AppContext.getServicioService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int userId = UserSession.getUserId(session);

		ViewRenderer.cargarError(request);
		cargarVista(request, response, userId);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(false);
		int userId = UserSession.getUserId(session);

		try {
			GuardarServicioRequest guardarServicioRequest = new GuardarServicioRequest(
					request.getParameter("nombre"),
					request.getParameter("precio"),
					request.getParameter("duracion"));

			servicioService.guardarServicio(userId, guardarServicioRequest);
			response.sendRedirect(request.getContextPath() + "/admin/servicios");
		} catch (ValidationException e) {
			ViewRenderer.redirectWithError(request, response, "/admin/servicios", e.getMessage());
		} catch (BusinessRuleException | ResourceNotFoundException | InternalSystemException e) {
			ViewRenderer.redirectWithError(request, response, "/admin/servicios", e.getMessage());
		}
	}

	private void cargarVista(HttpServletRequest request, HttpServletResponse response, int userId)
			throws ServletException, IOException {
		try {
			List<Servicio> servicios = servicioService.buscarPorDuenioDeNegocio(userId);

			request.setAttribute("servicios", servicios);
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
