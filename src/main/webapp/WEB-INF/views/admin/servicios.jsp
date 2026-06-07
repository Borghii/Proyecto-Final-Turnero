<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="servicio.entity.Servicio" %>
<%!
	private String escapeHtml(Object value) {
		if (value == null) {
			return "";
		}

		return value.toString()
				.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;")
				.replace("'", "&#39;");
	}

	private String escapeJs(Object value) {
		if (value == null) {
			return "";
		}

		String text = value.toString();
		StringBuilder escaped = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			switch (character) {
				case '\\':
					escaped.append("\\\\");
					break;
				case '\'':
					escaped.append("\\'");
					break;
				case '\r':
					escaped.append("\\r");
					break;
				case '\n':
					escaped.append("\\n");
					break;
				case '<':
					escaped.append("\\x3C");
					break;
				case '>':
					escaped.append("\\x3E");
					break;
				case '&':
					escaped.append("\\x26");
					break;
				default:
					escaped.append(character);
					break;
			}
		}

		return escaped.toString();
	}
%>
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

				<h2 id="form-titulo">Agregar servicio</h2>
				<form method="post" action="<%= request.getContextPath() %>/admin/servicios">
					<input type="hidden" id="accion" name="accion" value="guardar">
					<input type="hidden" id="idServicio" name="idServicio" value="0">

					<table>
						<tr>
							<td>Nombre:</td>
							<td><input type="text" id="nombre" name="nombre" required></td>
						</tr>
						<tr>
							<td>Precio:</td>
							<td><input type="number" id="precio" name="precio" step="0.01" min="0" required></td>
						</tr>
						<tr>
							<td>Duracion:</td>
							<td>
								<input type="number" id="duracion" name="duracion" step="30" min="30" required>
								minutos
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<button type="submit" id="btn-submit">Guardar servicio</button>
								<button type="button" onclick="resetForm()">Cancelar / limpiar</button>
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
							<th>Acciones</th>
						</tr>
						<%
							for (Servicio servicio : servicios) {
						%>
							<tr>
								<td><%= escapeHtml(servicio.getNombre()) %></td>
								<td>$<%= servicio.getPrecio() %></td>
								<td><%= servicio.getDuracion() %> min</td>
								<td>
									<button type="button" onclick="editarServicio(<%= servicio.getIdServicio() %>, '<%= escapeJs(servicio.getNombre()) %>', <%= servicio.getPrecio() %>, <%= servicio.getDuracion() %>)">Editar</button>

									<form method="post" action="<%= request.getContextPath() %>/admin/servicios" style="display: inline;" onsubmit="return confirm('Seguro que queres eliminar este servicio?');">
										<input type="hidden" name="accion" value="eliminar">
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

	<script>
		function editarServicio(id, nombre, precio, duracion) {
			document.getElementById('form-titulo').innerText = 'Editar servicio';
			document.getElementById('accion').value = 'editar';
			document.getElementById('idServicio').value = id;
			document.getElementById('nombre').value = nombre;
			document.getElementById('precio').value = precio;
			document.getElementById('duracion').value = duracion;
			document.getElementById('btn-submit').innerText = 'Guardar cambios';
			window.scrollTo(0, 0);
		}

		function resetForm() {
			document.getElementById('form-titulo').innerText = 'Agregar servicio';
			document.getElementById('accion').value = 'guardar';
			document.getElementById('idServicio').value = '0';
			document.getElementById('nombre').value = '';
			document.getElementById('precio').value = '';
			document.getElementById('duracion').value = '';
			document.getElementById('btn-submit').innerText = 'Guardar servicio';
		}
	</script>
</body>
</html>
