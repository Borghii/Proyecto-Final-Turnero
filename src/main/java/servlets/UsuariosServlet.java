package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Usuario;

@WebServlet("/usuarios")
public class UsuariosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String DB_URL = getEnv("DB_URL", "jdbc:mysql://localhost:3306/test");
	private static final String DB_USER = getEnv("DB_USER", "root");
	private static final String DB_PASSWORD = getEnv("DB_PASSWORD", "");

	private static String getEnv(String name, String defaultValue) {
		String value = System.getenv(name);
		return value == null || value.isBlank() ? defaultValue : value;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Usuario> usuarios = new ArrayList<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
					PreparedStatement ps = conn.prepareStatement("SELECT id, nombre, apellido FROM usuarios");
					ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					usuarios.add(new Usuario(
							rs.getInt("id"),
							rs.getString("nombre"),
							rs.getString("apellido")));
				}
			}

			request.setAttribute("usuarios", usuarios);
		} catch (Exception e) {
			request.setAttribute("error", e.getMessage());
		}

		RequestDispatcher rd = request.getRequestDispatcher("/usuarios.jsp");
		rd.forward(request, response);
	}
}
