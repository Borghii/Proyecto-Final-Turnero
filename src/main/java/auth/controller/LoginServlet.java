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
import auth.session.UserSession;
import auth.service.AuthService;
import shared.context.AppContext;
import shared.enums.RolEnum;
import shared.exception.AuthenticationException;
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
import usuario.entity.Usuario;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final AuthService authService = AppContext.getAuthService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (UserSession.isAuthenticated(session)) {
			RolEnum role = UserSession.getRole(session);
			if (role == null) {
				session.invalidate();
				response.sendRedirect(request.getContextPath() + "/login");
				return;
			}

			response.sendRedirect(getHomeUrl(request, role));
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
			UserSession.save(session, usuario);

			response.sendRedirect(getHomeUrl(request, usuario.getRole()));
		} catch (ValidationException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			forwardLoginWithError(request, response, e.getMessage());
		} catch (ResourceNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			forwardLoginWithError(request, response, e.getMessage());
		} catch (AuthenticationException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			forwardLoginWithError(request, response, e.getMessage());
		} catch (SQLException e) {
			getServletContext().log("Error de base de datos durante el login", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			forwardLoginWithError(request, response, "No se pudo completar el login por un error de base de datos");
		}
	}

	private String getHomeUrl(HttpServletRequest request, RolEnum role) {
		if (role == RolEnum.ADMIN) {
			return request.getContextPath() + "/admin/home.jsp";
		}

		if (role == RolEnum.CLIENTE) {
			return request.getContextPath() + "/cliente/home.jsp";
		}

		return request.getContextPath() + "/login";
	}

	private void forwardLoginWithError(HttpServletRequest request, HttpServletResponse response, String message)
			throws ServletException, IOException {
		request.setAttribute("error", message);
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}
}
