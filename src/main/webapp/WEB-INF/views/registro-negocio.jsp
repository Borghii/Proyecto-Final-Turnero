<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Registro de Negocio</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
	<center>
		<h1>Registrar mi negocio</h1>

		<%
			String error = (String) request.getAttribute("error");
			if (error != null) {
		%>
			<p style="color: red;"><b><%= error %></b></p>
		<%
			}
		%>

		<form method="post" action="<%= request.getContextPath() %>/signup/business">
			
			<h3>Datos del Administrador</h3>
			<table>
				<tr>
					<td>Tu Nombre:</td>
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
			</table>

			<hr style="width: 30%; margin-top: 15px; margin-bottom: 15px;">

			<h3>Datos del Local</h3>
			<table>
				<tr>
					<td>Nombre del negocio:</td>
					<td><input name="nombreNegocio" type="text" required></td>
				</tr>
				<tr>
					<td>Ciudad:</td>
					<td><input name="ciudad" type="text" required></td>
				</tr>
				
				<tr>
					<td style="padding-top: 20px;">
						<a href="<%= request.getContextPath() %>/login">
							<button type="button">Cancelar</button>
						</a>
					</td>
					<td style="padding-top: 20px;">
						<input type="submit" value="Crear negocio">
					</td>
				</tr>
			</table>
		</form>
	</center>
</body>
</html>
