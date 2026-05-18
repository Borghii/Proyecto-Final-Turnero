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

	// Inyectamos el servicio que acabamos de modificar
	private final NegocioService negocioService = AppContext.getNegocioService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// 1. Buscamos todas las peluquerías en la base de datos
			List<Negocio> listaNegocios = negocioService.obtenerTodosLosNegocios();
			
			// 2. Guardamos la lista en la "mochila" del request para que el JSP la pueda usar
			request.setAttribute("negocios", listaNegocios);
			
			// 3. Mandamos al usuario a la vista (el archivo JSP)
			ViewRenderer.forward(request, response, ViewPaths.CLIENTE_HOME);
			
		} catch (SQLException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.CLIENTE_HOME, "Error al cargar las peluquerías. Intentá de nuevo más tarde.");
		}
	}
}