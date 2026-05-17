package shared.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
}
