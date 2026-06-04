<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="negocio.dto.NegocioConHorarios" %>
<%@ page import="negocio.entity.Horario" %>
<%!
	private String nombreDia(int dia) {
		switch (dia) {
			case 1:
				return "Lunes";
			case 2:
				return "Martes";
			case 3:
				return "Miercoles";
			case 4:
				return "Jueves";
			case 5:
				return "Viernes";
			case 6:
				return "Sabado";
			case 7:
				return "Domingo";
			default:
				return "Dia " + dia;
		}
	}

	private void opcionesHora(javax.servlet.jsp.JspWriter out, LocalTime valor, LocalTime desde,
			LocalTime hasta) throws java.io.IOException {
		out.print("<option value=\"\">--</option>");

		if (valor != null && (valor.isBefore(desde) || valor.isAfter(hasta))) {
			out.print("<option value=\"" + valor + "\" selected>" + valor + "</option>");
		}

		for (LocalTime opcion = desde; ; opcion = opcion.plusMinutes(30)) {
			String selected = opcion.equals(valor) ? " selected" : "";
			out.print("<option value=\"" + opcion + "\"" + selected + ">" + opcion + "</option>");

			if (opcion.equals(hasta)) {
				break;
			}
		}
	}
%>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Mi negocio</title>
</head>
<body>
	<table width="100%">
		<tr>
			<td width="180" valign="top">
				<%@ include file="/WEB-INF/views/admin/partials/sidebar.jspf" %>
			</td>
			<td valign="top">
				<h1>Mi negocio</h1>

				<%
					String error = (String) request.getAttribute("error");
					if (error != null) {
				%>
					<p style="color: red;"><b><%= error %></b></p>
				<%
					}

					NegocioConHorarios negocio = (NegocioConHorarios) request.getAttribute("negocio");

					if (negocio == null) {
				%>
					<p>No se encontraron datos del negocio.</p>
				<%
					} else {
				%>
					<form method="post" action="<%= request.getContextPath() %>/admin/negocio">
						<h2>Datos generales</h2>

						<table>
							<tr>
								<td>Nombre:</td>
								<td><input type="text" name="nombre" value="<%= negocio.getNombre() %>"></td>
							</tr>
							<tr>
								<td>Ciudad:</td>
								<td><input type="text" name="ciudad" value="<%= negocio.getCiudad() %>"></td>
							</tr>
						</table>

						<h2>Horarios</h2>

						<%
							if (negocio.getHorarios() == null || negocio.getHorarios().isEmpty()) {
						%>
							<p>No hay horarios cargados.</p>
						<%
							} else {
						%>
							<table border="1">
								<tr>
									<th>Dia</th>
									<th>Activo</th>
									<th>Manana desde</th>
									<th>Manana hasta</th>
									<th>Tarde desde</th>
									<th>Tarde hasta</th>
								</tr>
								<%
									for (Horario horario : negocio.getHorarios()) {
								%>
									<tr>
										<td>
											<%= nombreDia(horario.getDia()) %>
											<input type="hidden" name="dia" value="<%= horario.getDia() %>">
										</td>
										<td>
											<input type="checkbox" name="activo_<%= horario.getDia() %>"
													<%= horario.isActivo() ? "checked" : "" %>>
										</td>
										<td>
											<select name="mananaDesde_<%= horario.getDia() %>">
												<% opcionesHora(out, horario.getMananaDesde(), LocalTime.of(0, 0), LocalTime.of(12, 0)); %>
											</select>
										</td>
										<td>
											<select name="mananaHasta_<%= horario.getDia() %>">
												<% opcionesHora(out, horario.getMananaHasta(), LocalTime.of(0, 0), LocalTime.of(12, 0)); %>
											</select>
										</td>
										<td>
											<select name="tardeDesde_<%= horario.getDia() %>">
												<% opcionesHora(out, horario.getTardeDesde(), LocalTime.of(12, 0), LocalTime.of(23, 30)); %>
											</select>
										</td>
										<td>
											<select name="tardeHasta_<%= horario.getDia() %>">
												<% opcionesHora(out, horario.getTardeHasta(), LocalTime.of(12, 0), LocalTime.of(23, 30)); %>
											</select>
										</td>
									</tr>
								<%
									}
								%>
							</table>
						<%
							}
						%>

						<br>
						<button type="submit" disabled>Guardar cambios</button>
					</form>
				<%
					}
				%>
			</td>
		</tr>
	</table>
</body>
</html>
