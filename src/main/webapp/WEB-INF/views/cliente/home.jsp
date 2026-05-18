<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="negocio.entity.Negocio" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inicio - Cliente</title>
    <style>
        .tarjeta {
            border: 1px solid #ccc;
            padding: 15px;
            margin: 10px;
            border-radius: 8px;
            width: 300px;
            display: inline-block;
        }
    </style>
</head>
<body>
    <center>
        <h1>Bienvenido a tu panel, <%= session.getAttribute("nombreUsuario") %></h1>
        <hr style="width: 50%;">

        <h2>Peluquerías Disponibles</h2>

        <%
            // Recuperamos la lista que nos mandó el Servlet
            List<Negocio> negocios = (List<Negocio>) request.getAttribute("negocios");
            
            if (negocios != null && !negocios.isEmpty()) {
                // Recorremos la lista
                for (Negocio n : negocios) {
        %>
                    <div class="tarjeta">
                        <h3><%= n.getNombre() %></h3>
                        <p><b>Ciudad:</b> <%= n.getCiudad() %></p>
                        
                        <a href="<%= request.getContextPath() %>/turnos?idNegocio=<%= n.getIdNegocio() %>">
                            <button type="button">Ver Turnos</button>
                        </a>
                    </div>
        <%
                }
            } else {
        %>
                <p>No hay peluquerías registradas por el momento.</p>
        <%
            }
        %>
    </center>
</body>
</html>