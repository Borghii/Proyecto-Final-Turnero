package auth.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.dto.SignUpClientRequest;
import auth.service.AuthService;
import shared.context.AppContext;
import shared.exception.InternalSystemException;
import shared.exception.ValidationException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;

@WebServlet("/signup/client")
public class SignUpClient extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final AuthService authService = AppContext.getAuthService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ViewRenderer.forward(request, response, ViewPaths.SIGNUP_CLIENT);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			SignUpClientRequest signUpClientRequest = new SignUpClientRequest(
					request.getParameter("mail"),
					request.getParameter("password"),
					request.getParameter("nombre"));

			authService.signUpClient(signUpClientRequest);

			response.sendRedirect(request.getContextPath() + "/login?registro=cliente");
		} catch (ValidationException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_BAD_REQUEST,
					ViewPaths.SIGNUP_CLIENT, e.getMessage());
		} catch (InternalSystemException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.SIGNUP_CLIENT, e.getMessage());
		}
	}
}
