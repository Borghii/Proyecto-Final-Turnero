<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Login</title>
</head>
<body>
	<center>
		<h1>Iniciar sesion</h1>

		<%
			String error = (String) request.getAttribute("error");
			if (error != null) {
		%>
			<p><b><%= error %></b></p>
		<%
			}
		%>

		<form method="post" action="<%= request.getContextPath() %>/login">
			<table>
				<tr>
					<td>Mail:</td>
					<td><input name="mail" type="text"></td>
				</tr>
				<tr>
					<td>Contrasena:</td>
					<td><input name="password" type="password"></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="Entrar"></td>
				</tr>
			</table>
		</form>
	</center>
</body>
</html>
