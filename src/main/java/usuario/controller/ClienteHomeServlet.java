package usuario.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import negocio.entity.Negocio;
import negocio.service.NegocioService;
import shared.context.AppContext;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;

@WebServlet("/cliente/home")
public class ClienteHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final NegocioService negocioService = AppContext.getNegocioService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			List<Negocio> listaNegocios = negocioService.obtenerTodosLosNegocios();
			
			request.setAttribute("negocios", listaNegocios);
			
			ViewRenderer.forward(request, response, ViewPaths.CLIENTE_HOME);
			
		} catch (SQLException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.CLIENTE_HOME, "Error al cargar las peluquerias. Intenta de nuevo mas tarde.");
		}
	}
}