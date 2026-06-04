<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="servicio.entity.Servicio" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Seleccionar Servicio</title>
	<style>
		.servicio-card {
			border: 1px solid #ccc;
			padding: 15px;
			margin: 10px 0;
			border-radius: 8px;
			background-color: #f9f9f9;
		}

		.accion-link {
			display: inline-block;
			padding: 8px 12px;
			background-color: #efefef;
			border: 1px solid #aaa;
			border-radius: 4px;
			color: #111;
			text-decoration: none;
		}
	</style>
</head>
<body>
	<table width="100%">
		<tr>
			<td width="180" valign="top">
				<%@ include file="/WEB-INF/views/cliente/partials/sidebar.jspf" %>
			</td>
			<td valign="top">
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
