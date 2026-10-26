<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="negocio.entity.Negocio" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inicio - Cliente</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <table class="layout">
        <tr>
            <td class="sidebar">
                <%@ include file="/WEB-INF/views/cliente/partials/sidebar.jspf" %>
            </td>

            <td class="contenido">
                <h1>Bienvenido a tu panel, <%= session.getAttribute("nombreUsuario") %></h1>
                <hr style="width: 50%; text-align: left; margin-left: 0;">

                <%
                    String error = (String) request.getAttribute("error");
                    if (error != null) {
                %>
                    <p style="color: red;"><%= error %></p>
                <%
                    }
                %>

                <h2>Peluquerias Disponibles</h2>

                <%
                    List<Negocio> negocios = (List<Negocio>) request.getAttribute("negocios");
                    if (negocios != null && !negocios.isEmpty()) {
                        for (Negocio n : negocios) {
                %>
                            <div class="tarjeta">
                                <h3><%= n.getNombre() %></h3>
                                <p><b>Ciudad:</b> <%= n.getCiudad() %></p>

                                <a class="accion-link" href="<%= request.getContextPath() %>/cliente/servicios?idNegocio=<%= n.getIdNegocio() %>">
                                    Ver servicios
                                </a>
                            </div>
                <%
                        }
                    } else {
                %>
                        <p>No hay peluquerias registradas por el momento.</p>
                <%
                    }
                %>
            </td>
        </tr>
    </table>
</body>
</html>
