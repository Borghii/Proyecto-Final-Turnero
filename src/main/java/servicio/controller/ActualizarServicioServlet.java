package servicio.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.session.UserSession;
import servicio.dto.ActualizarServicioRequest;
import servicio.service.ServicioService;
import shared.context.AppContext;
import shared.exception.AuthorizationException;
import shared.exception.BusinessRuleException;
import shared.exception.InternalSystemException;
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
import shared.web.ViewRenderer;

@WebServlet("/admin/servicios/actualizar")
public class ActualizarServicioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ServicioService servicioService = AppContext.getServicioService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int userId = UserSession.getUserId(session);

		try {
			ActualizarServicioRequest actualizarServicioRequest = new ActualizarServicioRequest(
					request.getParameter("idServicio"),
					request.getParameter("nombre"),
					request.getParameter("precio"),
					request.getParameter("duracion"));

			servicioService.actualizarServicio(userId, actualizarServicioRequest);
			response.sendRedirect(request.getContextPath() + "/admin/servicios");
		} catch (ValidationException e) {
			ViewRenderer.redirectWithError(request, response, "/admin/servicios", e.getMessage());
		} catch (BusinessRuleException e) {
			ViewRenderer.redirectWithError(request, response, "/admin/servicios", e.getMessage());
		} catch (AuthorizationException e) {
			ViewRenderer.redirectWithError(request, response, "/admin/servicios", e.getMessage());
		} catch (ResourceNotFoundException e) {
			ViewRenderer.redirectWithError(request, response, "/admin/servicios", e.getMessage());
		} catch (InternalSystemException e) {
			ViewRenderer.redirectWithError(request, response, "/admin/servicios", e.getMessage());
		}
	}
}
