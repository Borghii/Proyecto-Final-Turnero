<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="servicio.entity.Servicio" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Mis servicios</title>
</head>
<body>
	<table width="100%">
		<tr>
			<td width="180" valign="top">
				<%@ include file="/WEB-INF/views/admin/partials/sidebar.jspf" %>
			</td>
			<td valign="top">
				<h1>Mis servicios</h1>

				<%
					String error = (String) request.getAttribute("error");
					if (error != null) {
				%>
					<p style="color: red;"><b><%= error %></b></p>
				<%
					}
				%>

				<h2>Agregar servicio</h2>
				<form method="post" action="<%= request.getContextPath() %>/admin/servicios">
					<table>
						<tr>
							<td>Nombre:</td>
							<td><input type="text" name="nombre" required></td>
						</tr>
						<tr>
							<td>Precio:</td>
							<td><input type="number" name="precio" step="0.01" min="0" required></td>
						</tr>
						<tr>
							<td>Duracion:</td>
							<td>
								<input type="number" name="duracion" step="30" min="30" required>
								minutos
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<button type="submit">Guardar servicio</button>
							</td>
						</tr>
					</table>
				</form>

				<h2>Servicios actuales</h2>
				<%
					List<Servicio> servicios = (List<Servicio>) request.getAttribute("servicios");
					if (servicios == null || servicios.isEmpty()) {
				%>
					<p>No tenes servicios cargados.</p>
				<%
					} else {
				%>
					<table border="1">
						<tr>
							<th>Nombre</th>
							<th>Precio</th>
							<th>Duracion</th>
							<th>Guardar</th>
							<th>Borrar</th>
						</tr>
						<%
							for (Servicio servicio : servicios) {
								String editarFormId = "editar-servicio-" + servicio.getIdServicio();
						%>
							<tr>
								<td>
									<form id="<%= editarFormId %>" method="post" action="<%= request.getContextPath() %>/admin/servicios/actualizar"></form>
									<input form="<%= editarFormId %>" type="hidden" name="idServicio" value="<%= servicio.getIdServicio() %>">
									<input form="<%= editarFormId %>" type="text" name="nombre" value="<%= servicio.getNombre() %>" required>
								</td>
								<td>
									<input form="<%= editarFormId %>" type="number" name="precio" step="0.01" min="0" value="<%= servicio.getPrecio() %>" required>
								</td>
								<td>
									<input form="<%= editarFormId %>" type="number" name="duracion" step="30" min="30" value="<%= servicio.getDuracion() %>" required>
									min
								</td>
								<td>
									<button form="<%= editarFormId %>" type="submit">Guardar cambios</button>
								</td>
								<td>
									<form method="post" action="<%= request.getContextPath() %>/admin/servicios/eliminar">
										<input type="hidden" name="idServicio" value="<%= servicio.getIdServicio() %>">
										<button type="submit">Borrar</button>
									</form>
								</td>
							</tr>
						<%
							}
						%>
					</table>
				<%
					}
				%>
			</td>
		</tr>
	</table>
</body>
</html>
