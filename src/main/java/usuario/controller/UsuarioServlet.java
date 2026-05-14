package usuario.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shared.context.AppContext;
import usuario.service.UsuarioService;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final UsuarioService usuarioService = AppContext.getUsuarioService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
