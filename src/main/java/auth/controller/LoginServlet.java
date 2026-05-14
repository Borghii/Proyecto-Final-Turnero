package auth.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.dto.LoginRequest;
import auth.service.AuthService;
import shared.context.AppContext;
import shared.exception.AuthenticationException;
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
import usuario.entity.Usuario;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int ROL_CLIENTE = 1;
	private static final int ROL_ADMIN = 2;

	private final AuthService authService = AppContext.getAuthService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("idUsuario") != null) {
			response.sendRedirect(getHomeUrl(request, (Integer) session.getAttribute("idRolUsuario")));
			return;
		}

		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			LoginRequest loginRequest = new LoginRequest(
					request.getParameter("mail"),
					request.getParameter("password"));

			Usuario usuario = authService.login(loginRequest);
			HttpSession session = request.getSession(true);
			session.setAttribute("idUsuario", usuario.getIdUsuario());
			session.setAttribute("mailUsuario", usuario.getMail());
			session.setAttribute("nombreUsuario", usuario.getNombre());
			session.setAttribute("idRolUsuario", usuario.getIdRol());

			response.sendRedirect(getHomeUrl(request, usuario.getIdRol()));
		} catch (ValidationException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			forwardLoginConError(request, response, e.getMessage());
		} catch (ResourceNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			forwardLoginConError(request, response, e.getMessage());
		} catch (AuthenticationException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			forwardLoginConError(request, response, e.getMessage());
		} catch (SQLException e) {
			getServletContext().log("Error de base de datos durante el login", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			forwardLoginConError(request, response, "No se pudo completar el login por un error de base de datos");
		}
	}

	private String getHomeUrl(HttpServletRequest request, int idRol) {
		if (idRol == ROL_ADMIN) {
			return request.getContextPath() + "/admin/home.jsp";
		}

		if (idRol == ROL_CLIENTE) {
			return request.getContextPath() + "/cliente/home.jsp";
		}

		return request.getContextPath() + "/login";
	}

	private void forwardLoginConError(HttpServletRequest request, HttpServletResponse response, String mensaje)
			throws ServletException, IOException {
		request.setAttribute("error", mensaje);
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}
}
