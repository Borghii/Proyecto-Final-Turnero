<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="turno.dto.ActividadMesDTO" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Actividad</title>
</head>
<body>
	<table width="100%">
		<tr>
			<td width="180" valign="top">
				<%@ include file="/WEB-INF/views/admin/partials/sidebar.jspf" %>
			</td>
			<td valign="top">
				<h1>Actividad</h1>
				<p>Resumen del mes actual.</p>

				<%
					String error = (String) request.getAttribute("error");
					ActividadMesDTO actividad = (ActividadMesDTO) request.getAttribute("actividad");

					if (error != null) {
				%>
					<p style="color: red;"><b><%= error %></b></p>
				<%
					} else if (actividad == null) {
				%>
					<p>No hay datos de actividad para mostrar.</p>
				<%
					} else {
				%>
					<table border="1">
						<tr>
							<th>Total de turnos del mes</th>
							<th>Turnos realizados</th>
							<th>Turnos cancelados</th>
							<th>Dinero generado</th>
						</tr>
						<tr>
							<td><%= actividad.getTotalTurnos() %></td>
							<td><%= actividad.getTurnosRealizados() %></td>
							<td><%= actividad.getTurnosCancelados() %></td>
							<td>$<%= String.format("%.2f", actividad.getIngresosMes()) %></td>
						</tr>
					</table>
				<%
					}
				%>
			</td>
		</tr>
	</table>
</body>
</html>
