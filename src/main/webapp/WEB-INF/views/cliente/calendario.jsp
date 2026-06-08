<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Elegir Fecha y Hora</title>
</head>
<body>
	<table width="100%">
		<tr>
			<td width="180" valign="top">
				<%@ include file="/WEB-INF/views/cliente/partials/sidebar.jspf" %>
			</td>

			<td valign="top">
				<h1>Paso 2: Elegi la fecha y hora</h1>

				<%
					String error = (String) request.getAttribute("error");
					if (error != null) {
				%>
					<p style="color: red;"><b><%= error %></b></p>
				<%
					}
				%>

				<%
					Integer idNegocio = (Integer) request.getAttribute("idNegocio");
					Integer idServicio = (Integer) request.getAttribute("idServicio");
					LocalDate fechaSeleccionada = (LocalDate) request.getAttribute("fechaSeleccionada");
					List<LocalTime> horariosLibres = (List<LocalTime>) request.getAttribute("horariosLibres");
				%>

				<form method="GET" action="<%= request.getContextPath() %>/cliente/turnos/calendario">
					<input type="hidden" name="idNegocio" value="<%= idNegocio %>">
					<input type="hidden" name="idServicio" value="<%= idServicio %>">

					<label for="fecha"><b>Seleccionar otro dia: </b></label>
					<input type="date" id="fecha" name="fecha" value="<%= fechaSeleccionada %>" min="<%= LocalDate.now() %>" required>
					<button type="submit">Buscar horarios</button>
				</form>

				<h2>Horarios disponibles para el <%= fechaSeleccionada %></h2>

				<%
					if (horariosLibres == null || horariosLibres.isEmpty()) {
				%>
					<p style="color: red;">No hay turnos disponibles para esta fecha. Proba buscando otro dia.</p>
				<%
					} else {
				%>
					<table border="1">
						<tr>
							<th>Hora</th>
							<th>Accion</th>
						</tr>
						<%
							for (LocalTime hora : horariosLibres) {
						%>
							<tr>
								<td><%= hora %></td>
								<td>
									<a href="<%= request.getContextPath() %>/cliente/turnos/confirmar?idNegocio=<%= idNegocio %>&idServicio=<%= idServicio %>&fecha=<%= fechaSeleccionada %>&hora=<%= hora %>">
										Seleccionar
									</a>
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
