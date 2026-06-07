<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="shared.enums.EstadoTurnoEnum" %>
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
				<p>Historial y proximas citas de tu cuenta.</p>

				<%
					String error = (String) request.getAttribute("error");
					if (error != null) {
				%>
					<p style="color: red;"><b><%= error %></b></p>
				<%
					}
				%>

				<%
					List<TurnoClienteDTO> turnos = (List<TurnoClienteDTO>) request.getAttribute("turnos");
					if (turnos == null || turnos.isEmpty()) {
				%>
					<p>Todavia no realizaste ninguna reserva.</p>
				<%
					} else {
				%>
					<table border="1">
						<tr>
							<th>Peluqueria</th>
							<th>Servicio</th>
							<th>Precio</th>
							<th>Fecha</th>
							<th>Hora Inicio</th>
							<th>Hora Fin</th>
							<th>Estado</th>
							<th>Acciones</th>
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
								<td>
									<%
										if (t.getEstado() == EstadoTurnoEnum.CANCELADO) {
									%>
										Cancelado
									<%
										} else {
									%>
										<form method="post" action="<%= request.getContextPath() %>/cliente/turnos/cancelar">
											<input type="hidden" name="idTurno" value="<%= t.getIdTurno() %>">
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
