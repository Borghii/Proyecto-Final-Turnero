<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="shared.enums.EstadoTurnoEnum" %>
<%@ page import="turno.dto.TurnoConServicio" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Panel admin</title>
</head>
<body>
	<table width="100%">
		<tr>
			<td width="180" valign="top">
				<%@ include file="/WEB-INF/views/admin/partials/sidebar.jspf" %>
			</td>
			<td valign="top">
				<h1>Home</h1>
				<p>Hola, <%= session.getAttribute("nombreUsuario") %>.</p>

				<%
					String error = (String) request.getAttribute("error");
					if (error != null) {
				%>
					<p style="color: red;"><b><%= error %></b></p>
				<%
					}
				%>

				<h2>Proximos turnos</h2>

				<%
					List<TurnoConServicio> turnos = (List<TurnoConServicio>) request.getAttribute("turnos");
					if (turnos == null || turnos.isEmpty()) {
				%>
					<p>No tenes proximos turnos.</p>
				<%
					} else {
				%>
					<table border="1">
						<tr>
							<th>Fecha</th>
							<th>Hora inicio</th>
							<th>Hora fin</th>
							<th>Servicio</th>
							<th>Email cliente</th>
							<th>Nombre cliente</th>
							<th>Estado</th>
							<th>Acciones</th>
						</tr>
						<%
							for (TurnoConServicio turno : turnos) {
						%>
							<tr>
								<td><%= turno.getFecha() %></td>
								<td><%= turno.getHoraInicio() %></td>
								<td><%= turno.getHoraFin() %></td>
								<td><%= turno.getNombreServicio() %></td>
								<td><%= turno.getMailCliente() %></td>
								<td><%= turno.getNombreCliente() %></td>
								<td><%= turno.getEstado() %></td>
								<td>
									<%
										if (turno.getEstado() == EstadoTurnoEnum.CANCELADO) {
									%>
										Cancelado
									<%
										} else {
									%>
										<form method="post" action="<%= request.getContextPath() %>/admin/turnos/cancelar">
											<input type="hidden" name="idTurno" value="<%= turno.getIdTurno() %>">
											<button type="submit">Cancelar</button>
										</form>
									<%
										}
									%>
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
