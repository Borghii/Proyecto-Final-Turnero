package usuario.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import negocio.entity.Negocio;
import negocio.service.NegocioService;
import shared.context.AppContext;
import shared.exception.InternalSystemException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;

@WebServlet("/cliente/home")
public class ClienteHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final NegocioService negocioService = AppContext.getNegocioService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		cargarError(request, session);

		try {
			List<Negocio> listaNegocios = negocioService.obtenerTodosLosNegocios();
			
			request.setAttribute("negocios", listaNegocios);
			
			ViewRenderer.forward(request, response, ViewPaths.CLIENTE_HOME);
			
		} catch (InternalSystemException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.CLIENTE_HOME, e.getMessage());
		}
	}

	private void cargarError(HttpServletRequest request, HttpSession session) {
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
