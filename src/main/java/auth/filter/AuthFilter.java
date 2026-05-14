package auth.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/admin/*", "/cliente/*" })
public class AuthFilter implements Filter {
	private static final int ROL_CLIENTE = 1;
	private static final int ROL_ADMIN = 2;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("idUsuario") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		int idRol = (Integer) session.getAttribute("idRolUsuario");
		String uri = request.getRequestURI();

		if (uri.contains("/admin/") && idRol != ROL_ADMIN) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tenes permisos para acceder al panel de administrador");
			return;
		}

		if (uri.contains("/cliente/") && idRol != ROL_CLIENTE) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tenes permisos para acceder al panel de cliente");
			return;
		}

		chain.doFilter(request, response);
	}
}
