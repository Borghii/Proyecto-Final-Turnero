package auth.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.dto.SignUpClientRequest;
import auth.service.AuthService;
import shared.context.AppContext;
import shared.exception.ValidationException;

@WebServlet("/signup/client")
public class SignUpClient extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final AuthService authService = AppContext.getAuthService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/registro-cliente.jsp").forward(request, response);
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

			response.sendRedirect(request.getContextPath() + "/login");
		} catch (ValidationException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			forwardSignUpWithError(request, response, e.getMessage());
		} catch (SQLException e) {
			getServletContext().log("Error de base de datos durante el registro de cliente", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			forwardSignUpWithError(request, response, "No se pudo completar el registro por un error de base de datos");
		}
	}

	private void forwardSignUpWithError(HttpServletRequest request, HttpServletResponse response, String message)
			throws ServletException, IOException {
		request.setAttribute("error", message);
		request.getRequestDispatcher("/WEB-INF/views/registro-cliente.jsp").forward(request, response);
	}
}
