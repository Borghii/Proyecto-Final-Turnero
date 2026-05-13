package negocio.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import negocio.service.NegocioService;
import shared.context.AppContext;

@WebServlet("/negocios")
public class NegocioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final NegocioService negocioService = AppContext.getNegocioService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().println("NegocioServlet listo");
	}
}
