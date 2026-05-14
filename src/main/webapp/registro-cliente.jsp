<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Registro de Cliente</title>
</head>
<body>
	<center>
		<h1>Crear cuenta de Cliente</h1>

		<%-- Bloque para mostrar errores por si el Servlet rechaza el registro (ej. mail repetido) --%>
		<%
			String error = (String) request.getAttribute("error");
			if (error != null) {
		%>
			<p style="color: red;"><b><%= error %></b></p>
		<%
			}
		%>

		<form method="post" action="<%= request.getContextPath() %>/signup/client">
			<table>
				<tr>
					<td>Nombre:</td>
					<td><input name="nombre" type="text" required></td>
				</tr>
				<tr>
					<td>Mail:</td>
					<td><input name="mail" type="email" required></td>
				</tr>
				<tr>
					<td>Contraseña:</td>
					<td><input name="password" type="password" required></td>
				</tr>
				<tr>
					<td style="padding-top: 15px;">
						<a href="<%= request.getContextPath() %>/login.jsp">
							<button type="button">Cancelar</button>
						</a>
					</td>
					<td style="padding-top: 15px;">
						<input type="submit" value="Crear usuario">
					</td>
				</tr>
			</table>
		</form>
	</center>
</body>
</html>