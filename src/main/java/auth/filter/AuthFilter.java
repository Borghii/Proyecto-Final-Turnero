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

import auth.session.UserSession;
import shared.enums.RolEnum;

@WebFilter(urlPatterns = { "/admin/*", "/cliente/*" })
public class AuthFilter implements Filter {
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession(false);

		if (!UserSession.isAuthenticated(session)) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		RolEnum role = UserSession.getRole(session);
		if (role == null) {
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String uri = request.getRequestURI();

		if (uri.contains("/admin/") && role != RolEnum.ADMIN) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tenes permisos para acceder al panel de administrador");
			return;
		}

		if (uri.contains("/cliente/") && role != RolEnum.CLIENTE) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tenes permisos para acceder al panel de cliente");
			return;
		}

		chain.doFilter(request, response);
	}
}
