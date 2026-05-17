package auth.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shared.web.ViewPaths;
import shared.web.ViewRenderer;

@WebServlet("/signup/business")
public class SignUpBusiness extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ViewRenderer.forward(request, response, ViewPaths.SIGNUP_BUSINESS);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_NOT_IMPLEMENTED,
				ViewPaths.SIGNUP_BUSINESS, "El registro de negocio todavia no esta disponible");
	}
}
