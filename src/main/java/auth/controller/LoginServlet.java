package auth.controller;

import java.io.IOException;

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
import shared.exception.InternalSystemException;
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;
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

		ViewRenderer.forward(request, response, ViewPaths.LOGIN);
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
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_BAD_REQUEST,
					ViewPaths.LOGIN, e.getMessage());
		} catch (ResourceNotFoundException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_NOT_FOUND,
					ViewPaths.LOGIN, e.getMessage());
		} catch (AuthenticationException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_UNAUTHORIZED,
					ViewPaths.LOGIN, e.getMessage());
		} catch (InternalSystemException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.LOGIN, e.getMessage());
		}
	}

	private String getHomeUrl(HttpServletRequest request, RolEnum role) {
		if (role == RolEnum.ADMIN) {
			return request.getContextPath() + "/admin/home";
		}

		if (role == RolEnum.CLIENTE) {
			return request.getContextPath() + "/cliente/home";
		}

		return request.getContextPath() + "/login";
	}
}
