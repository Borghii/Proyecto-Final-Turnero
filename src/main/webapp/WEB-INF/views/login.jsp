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
			String registro = request.getParameter("registro");
			if ("cliente".equals(registro)) {
		%>
			<p style="color: green;"><b>Registro exitoso. Ya podes iniciar sesion como cliente.</b></p>
		<%
			} else if ("negocio".equals(registro)) {
		%>
			<p style="color: green;"><b>Registro exitoso. Ya podes iniciar sesion como administrador.</b></p>
		<%
			}
		%>

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
		
		<br>
		<hr style="width: 30%;">
		<br>
		
		<h3>No tenes cuenta?</h3>
		<table>
			<tr>
				<td style="padding-right: 20px;">
					<a href="<%= request.getContextPath() %>/signup/client">
						<button type="button">Registrarme como cliente</button>
					</a>
				</td>
				<td>
					<a href="<%= request.getContextPath() %>/signup/business">
						<button type="button">Registrar mi negocio</button>
					</a>
				</td>
			</tr>
		</table>
	</center>
</body>
</html>
