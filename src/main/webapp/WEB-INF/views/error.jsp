<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Error</title>
</head>
<body>
	<h1>Error</h1>

	<%
		String error = (String) request.getAttribute("error");
		if (error != null) {
	%>
		<p style="color: red;"><b><%= error %></b></p>
	<%
		}
	%>

	<p><a href="<%= request.getContextPath() %>/login">Volver al inicio</a></p>
</body>
</html>
