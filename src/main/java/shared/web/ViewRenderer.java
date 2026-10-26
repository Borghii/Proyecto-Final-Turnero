package shared.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class ViewRenderer {
	private ViewRenderer() {
	}

	public static void forward(HttpServletRequest request, HttpServletResponse response, String viewPath)
			throws ServletException, IOException {
		request.getRequestDispatcher(viewPath).forward(request, response);
	}

	public static void forwardWithError(HttpServletRequest request, HttpServletResponse response, int statusCode,
			String viewPath, String message) throws ServletException, IOException {
		response.setStatus(statusCode);
		request.setAttribute("error", message);
		forward(request, response, viewPath);
	}

	public static void redirectWithError(HttpServletRequest request, HttpServletResponse response, String path,
			String message) throws IOException {
		HttpSession session = request.getSession();
		session.setAttribute("error", message);
		response.sendRedirect(request.getContextPath() + path);
	}

	public static void cargarError(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		String error = (String) session.getAttribute("error");
		if (error != null) {
			request.setAttribute("error", error);
			session.removeAttribute("error");
		}
	}
}
