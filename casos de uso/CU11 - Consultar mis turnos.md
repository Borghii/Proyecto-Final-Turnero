# CU11 - Consultar mis turnos

FLUJO DE SUCESOS:
Camino Basico:

1. El cliente entra a "Mis turnos".
2. El sistema busca los turnos del cliente.
3. El sistema muestra la lista de turnos.
4. De cada turno se muestra negocio, servicio, precio, fecha, hora de inicio, hora de fin y estado.
5. El cliente revisa sus reservas.

Caminos Alternativos:

2.a El cliente no tiene turnos registrados.
2.a.1 El sistema muestra el mensaje "Todavia no realizaste ninguna reserva".

2.b No se pueden cargar los turnos.
2.b.1 El sistema muestra un mensaje de error.
