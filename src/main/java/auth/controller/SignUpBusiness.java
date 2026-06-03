package auth.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.dto.SignUpBussinesRequest;
import auth.dto.SignUpClientRequest;
import auth.service.AuthService;
import shared.context.AppContext;
import shared.exception.InternalSystemException;
import shared.exception.ValidationException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;

@WebServlet("/signup/business")
public class SignUpBusiness extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final AuthService authService = AppContext.getAuthService();


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ViewRenderer.forward(request, response, ViewPaths.SIGNUP_BUSINESS);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			SignUpClientRequest signUpClientRequest = new SignUpClientRequest(
					request.getParameter("mail"),
					request.getParameter("password"),
					request.getParameter("nombre"));

			SignUpBussinesRequest signUpBussinesRequest = new SignUpBussinesRequest(
					signUpClientRequest,
					request.getParameter("nombreNegocio"),
					request.getParameter("ciudad")
			);

			authService.signUpBusiness(signUpBussinesRequest);

			response.sendRedirect(request.getContextPath() + "/login");
		} catch (ValidationException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_BAD_REQUEST,
					ViewPaths.SIGNUP_BUSINESS, e.getMessage());
		} catch (InternalSystemException e) {
			ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ViewPaths.SIGNUP_BUSINESS, e.getMessage());
		}



	}
}
