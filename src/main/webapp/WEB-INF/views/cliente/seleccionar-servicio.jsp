<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="servicio.entity.Servicio" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Seleccionar Servicio</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
	<table class="layout">
		<tr>
			<td class="sidebar">
				<%@ include file="/WEB-INF/views/cliente/partials/sidebar.jspf" %>
			</td>
			<td class="contenido">
				<h1>Paso 1: Elegi un servicio</h1>
				<hr style="width: 50%; text-align: left; margin-left: 0;">

				<%
					List<Servicio> servicios = (List<Servicio>) request.getAttribute("servicios");
					Integer idNegocio = (Integer) request.getAttribute("idNegocio");

					if (servicios == null || servicios.isEmpty()) {
				%>
					<p>Este negocio todavia no tiene servicios cargados.</p>
					<a href="<%= request.getContextPath() %>/cliente/home">Volver atras</a>
				<%
					} else {
						for (Servicio s : servicios) {
				%>
							<div class="servicio-card">
								<h3><%= s.getNombre() %></h3>
								<p><b>Precio:</b> $<%= s.getPrecio() %> | <b>Duracion:</b> <%= s.getDuracion() %> min</p>

								<a class="accion-link" href="<%= request.getContextPath() %>/cliente/turnos/calendario?idServicio=<%= s.getIdServicio() %>&idNegocio=<%= idNegocio %>">
									Seleccionar este servicio
								</a>
							</div>
				<%
						}
					}
				%>
			</td>
		</tr>
	</table>
</body>
</html>
