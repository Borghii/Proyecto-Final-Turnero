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
import shared.exception.ResourceNotFoundException;

@WebServlet("/admin/servicios")
public class ServicioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ServicioService servicioService = AppContext.getServicioService();
	private final NegocioService negocioService = AppContext.getNegocioService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Negocio negocio = obtenerNegocioDelUsuario(request, response);
			if (negocio == null) {
				return;
			}

			List<Servicio> servicios = servicioService.buscarPorNegocio(negocio.getIdNegocio());
			request.setAttribute("servicios", servicios);
			request.setAttribute("idNegocio", negocio.getIdNegocio());
		} catch (ResourceNotFoundException e) {
			request.setAttribute("error", e.getMessage());
		} catch (Exception e) {
			request.setAttribute("error", "Error al cargar los servicios.");
		}

		request.getRequestDispatcher("/WEB-INF/views/admin/servicios.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		try {
			Negocio negocio = obtenerNegocioDelUsuario(request, response);
			if (negocio == null) {
				return;
			}

			String accion = request.getParameter("accion");
			if ("guardar".equals(accion)) {
				Servicio nuevo = new Servicio(
						0,
						request.getParameter("nombre"),
						Double.parseDouble(request.getParameter("precio")),
						Integer.parseInt(request.getParameter("duracion")),
						negocio.getIdNegocio());
				servicioService.guardarServicio(nuevo);
			} else if ("eliminar".equals(accion)) {
				int idServicio = Integer.parseInt(request.getParameter("idServicio"));
				servicioService.eliminarServicio(idServicio, negocio.getIdNegocio());
			} else if ("editar".equals(accion)) {
				Servicio editado = new Servicio(
						Integer.parseInt(request.getParameter("idServicio")),
						request.getParameter("nombre"),
						Double.parseDouble(request.getParameter("precio")),
						Integer.parseInt(request.getParameter("duracion")),
						negocio.getIdNegocio());
				servicioService.actualizarServicio(editado);
			}

			response.sendRedirect(request.getContextPath() + "/admin/servicios");
		} catch (BusinessRuleException | AuthorizationException | ResourceNotFoundException e) {
			request.setAttribute("error", e.getMessage());
			doGet(request, response);
		} catch (Exception e) {
			request.setAttribute("error", "Ocurrio un error al procesar la solicitud. Revisa los datos.");
			doGet(request, response);
		}
	}

	private Negocio obtenerNegocioDelUsuario(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(false);
		if (session == null || !UserSession.isAuthenticated(session)) {
			response.sendRedirect(request.getContextPath() + "/login");
			return null;
		}

		return negocioService.obtenerNegocioPorUsuario(UserSession.getUserId(session));
	}
}
