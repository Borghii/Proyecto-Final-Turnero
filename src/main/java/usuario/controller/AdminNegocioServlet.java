package usuario.controller;


import auth.session.UserSession;
import negocio.dto.NegocioConHorarios;
import negocio.service.NegocioService;
import shared.context.AppContext;
import shared.exception.InternalSystemException;
import shared.exception.ResourceNotFoundException;
import shared.web.ViewPaths;
import shared.web.ViewRenderer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/negocio")
public class AdminNegocioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final NegocioService negocioService = AppContext.getNegocioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        HttpSession session = request.getSession(false);

        int userId = UserSession.getUserId(session);

        try {
            NegocioConHorarios negocioConHorarios = negocioService.obtenerNegocioConHorarios(userId);

            request.setAttribute("negocio", negocioConHorarios);

            ViewRenderer.forward(request, response, ViewPaths.ADMIN_NEGOCIO);

        } catch (ResourceNotFoundException e) {
            ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_NOT_FOUND,
                    ViewPaths.ADMIN_NEGOCIO, e.getMessage());
        } catch (InternalSystemException e) {
            ViewRenderer.forwardWithError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ViewPaths.ADMIN_NEGOCIO, e.getMessage());
        }


    }
}
