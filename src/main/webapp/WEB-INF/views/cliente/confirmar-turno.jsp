<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="turno.dto.ConfirmacionTurnoDTO" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Confirmar Turno</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
	<table class="layout">
		<tr>
			<td class="sidebar">
				<%@ include file="/WEB-INF/views/cliente/partials/sidebar.jspf" %>
			</td>
			<td class="contenido">
				<h1>Paso 3: Confirmar turno</h1>
				<hr style="width: 50%; text-align: left; margin-left: 0;">

				<%
					ConfirmacionTurnoDTO confirmacion = (ConfirmacionTurnoDTO) request.getAttribute("confirmacion");
					if (confirmacion == null) {
				%>
					<p>No se pudo cargar el resumen del turno.</p>
					<a href="<%= request.getContextPath() %>/cliente/home">Volver al inicio</a>
				<%
					} else {
				%>
					<div class="resumen">
						<h3><%= confirmacion.getNombreServicio() %></h3>
						<p><b>Fecha:</b> <%= confirmacion.getFecha() %></p>
						<p><b>Horario:</b> <%= confirmacion.getHoraInicio() %> a <%= confirmacion.getHoraFin() %></p>
						<p><b>Duracion:</b> <%= confirmacion.getDuracion() %> min</p>
						<p><b>Precio:</b> $<%= confirmacion.getPrecio() %></p>
					</div>

					<div class="acciones">
						<a href="<%= request.getContextPath() %>/cliente/turnos/calendario?idNegocio=<%= confirmacion.getIdNegocio() %>&idServicio=<%= confirmacion.getIdServicio() %>&fecha=<%= confirmacion.getFecha() %>">
							Volver
						</a>

						<form method="post" action="<%= request.getContextPath() %>/cliente/turnos/confirmar" style="display: inline;">
							<input type="hidden" name="idNegocio" value="<%= confirmacion.getIdNegocio() %>">
							<input type="hidden" name="idServicio" value="<%= confirmacion.getIdServicio() %>">
							<input type="hidden" name="fecha" value="<%= confirmacion.getFecha() %>">
							<input type="hidden" name="hora" value="<%= confirmacion.getHoraInicio() %>">
							<button type="submit">Confirmar reserva</button>
						</form>
					</div>
				<%
					}
				%>
			</td>
		</tr>
	</table>
</body>
</html>
