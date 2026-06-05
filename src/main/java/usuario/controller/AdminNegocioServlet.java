package usuario.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.session.UserSession;
import negocio.dto.ActualizarHorarioRequest;
import negocio.dto.ActualizarNegocioRequest;
import negocio.dto.NegocioConHorarios;
import negocio.service.NegocioService;
import shared.context.AppContext;
import shared.exception.BusinessRuleException;
import shared.exception.InternalSystemException;
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;

@WebServlet("/admin/negocio")
public class AdminNegocioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final NegocioService negocioService = AppContext.getNegocioService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int userId = UserSession.getUserId(session);

		cargarVista(request, response, userId);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int userId = UserSession.getUserId(session);

		try {
			ActualizarNegocioRequest actualizarNegocioRequest = mapRequest(request);
			negocioService.actualizarNegocio(userId, actualizarNegocioRequest);
			response.sendRedirect(request.getContextPath() + "/admin/negocio?actualizado=1");
		} catch (ValidationException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			request.setAttribute("error", e.getMessage());
			cargarVista(request, response, userId);
		} catch (BusinessRuleException e) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			request.setAttribute("error", e.getMessage());
			cargarVista(request, response, userId);
		} catch (ResourceNotFoundException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_NOT_FOUND,
					ViewPaths.ADMIN_NEGOCIO, e.getMessage());
		} catch (InternalSystemException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.ADMIN_NEGOCIO, e.getMessage());
		}
	}

	private ActualizarNegocioRequest mapRequest(HttpServletRequest request) {
		List<ActualizarHorarioRequest> horarios = new ArrayList<>();
		String[] dias = request.getParameterValues("dia");

		if (dias != null) {
			for (String dia : dias) {
				String sufijo = dia.trim();
				horarios.add(new ActualizarHorarioRequest(
						dia,
						request.getParameter("activo_" + sufijo) != null,
						request.getParameter("mananaDesde_" + sufijo),
						request.getParameter("mananaHasta_" + sufijo),
						request.getParameter("tardeDesde_" + sufijo),
						request.getParameter("tardeHasta_" + sufijo)));
			}
		}

		return new ActualizarNegocioRequest(
				request.getParameter("nombre"),
				request.getParameter("ciudad"),
				horarios);
	}

	private void cargarVista(HttpServletRequest request, HttpServletResponse response, int userId)
			throws ServletException, IOException {
		try {
			NegocioConHorarios negocioConHorarios = negocioService.obtenerNegocioConHorarios(userId);
			request.setAttribute("negocio", negocioConHorarios);
			ViewRenderer.forward(request, response, ViewPaths.ADMIN_NEGOCIO);
		} catch (ResourceNotFoundException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_NOT_FOUND,
					ViewPaths.ADMIN_NEGOCIO, e.getMessage());
		} catch (InternalSystemException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.ADMIN_NEGOCIO, e.getMessage());
		}
	}
}
