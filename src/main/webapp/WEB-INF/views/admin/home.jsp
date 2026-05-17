<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Panel admin</title>
</head>
<body>
	<center>
		<h1>Panel de administrador</h1>
		<p>Hola, <%= session.getAttribute("nombreUsuario") %>.</p>
		<p>Mail: <%= session.getAttribute("mailUsuario") %></p>
		<p><a href="<%= request.getContextPath() %>/logout">Cerrar sesion</a></p>
	</center>
</body>
</html>
