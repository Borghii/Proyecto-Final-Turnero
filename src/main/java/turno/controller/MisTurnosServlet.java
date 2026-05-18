package turno.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import shared.context.AppContext;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;
import turno.dto.TurnoClienteDTO;
import turno.service.TurnoService;

@WebServlet("/cliente/turnos")
public class MisTurnosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final TurnoService turnoService = AppContext.getTurnoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        // Si no hay sesión o no tiene ID, lo mandamos al login por seguridad
        if (session == null || session.getAttribute("idUsuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int idUsuario = (Integer) session.getAttribute("idUsuario");

        try {
            List<TurnoClienteDTO> misTurnos = turnoService.obtenerTurnosPorCliente(idUsuario);
            request.setAttribute("turnos", misTurnos);
            
            // Creá esta constante en tu clase ViewPaths o poné la ruta directamente
            request.getRequestDispatcher("/WEB-INF/views/cliente/mis-turnos.jsp").forward(request, response);
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.setAttribute("error", "Error de base de datos al cargar tus turnos.");
            request.getRequestDispatcher("/WEB-INF/views/cliente/home.jsp").forward(request, response);
        }
    }
}