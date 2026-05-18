<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="turno.dto.TurnoClienteDTO" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Mis Turnos</title>
</head>
<body>
	<table width="100%">
		<tr>
			<td width="180" valign="top">
				<%@ include file="/WEB-INF/views/cliente/partials/sidebar.jspf" %>
			</td>
			<td valign="top">
				<h1>Mis Turnos Reservados</h1>
				<p>Historial y próximas citas de tu cuenta.</p>

				<%
					List<TurnoClienteDTO> turnos = (List<TurnoClienteDTO>) request.getAttribute("turnos");
					if (turnos == null || turnos.isEmpty()) {
				%>
					<p>Todavía no realizaste ninguna reserva.</p>
				<%
					} else {
				%>
					<table border="1" cellpadding="8" style="border-collapse: collapse; width: 90%;">
						<tr style="background-color: #f2f2f2;">
							<th>Peluquería</th>
							<th>Servicio</th>
							<th>Precio</th>
							<th>Fecha</th>
							<th>Hora Inicio</th>
							<th>Hora Fin</th>
							<th>Estado</th>
						</tr>
						<%
							for (TurnoClienteDTO t : turnos) {
						%>
							<tr>
								<td><b><%= t.getNombreNegocio() %></b></td>
								<td><%= t.getNombreServicio() %></td>
								<td>$<%= t.getPrecio() %></td>
								<td><%= t.getFecha() %></td>
								<td><%= t.getHoraInicio() %></td>
								<td><%= t.getHoraFin() %></td>
								<td><%= t.getEstado() %></td>
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