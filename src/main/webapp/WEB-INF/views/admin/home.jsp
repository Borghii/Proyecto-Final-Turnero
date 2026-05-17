<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<title>Panel admin</title>
</head>
<body>
	<table width="100%">
		<tr>
			<td width="180" valign="top">
				<%@ include file="/WEB-INF/views/admin/partials/sidebar.jspf" %>
			</td>
			<td valign="top">
				<h1>Home</h1>
				<p>Hola, <%= session.getAttribute("nombreUsuario") %>.</p>

				<h2>Próximos turnos</h2>
				<p>No tenés próximos turnos.</p>
			</td>
		</tr>
	</table>
</body>
</html>
