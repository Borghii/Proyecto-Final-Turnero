<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Usuario" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Usuarios</title>
<style>
	body {
		font-family: Arial, sans-serif;
		margin: 32px;
	}

	table {
		border-collapse: collapse;
		min-width: 420px;
	}

	th,
	td {
		border: 1px solid #ccc;
		padding: 8px 12px;
		text-align: left;
	}

	th {
		background: #f2f2f2;
	}

	.error {
		color: #b00020;
		font-weight: bold;
	}
</style>
</head>
<body>
	<h1>Usuarios</h1>

	<%
	String error = (String) request.getAttribute("error");
	if (error != null) {
	%>
		<p class="error">Error al conectar o consultar la base: <%= error %></p>
	<%
	} else {
		List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
	%>
		<table>
			<thead>
				<tr>
					<th>ID</th>
					<th>Nombre</th>
					<th>Apellido</th>
				</tr>
			</thead>
			<tbody>
				<%
				if (usuarios != null && !usuarios.isEmpty()) {
					for (Usuario usuario : usuarios) {
				%>
					<tr>
						<td><%= usuario.getId() %></td>
						<td><%= usuario.getNombre() %></td>
						<td><%= usuario.getApellido() %></td>
					</tr>
				<%
					}
				} else {
				%>
					<tr>
						<td colspan="3">No hay usuarios cargados.</td>
					</tr>
				<%
				}
				%>
			</tbody>
		</table>
	<%
	}
	%>
</body>
</html>
