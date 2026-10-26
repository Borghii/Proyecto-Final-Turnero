# CU03 - Consultar proximos turnos del negocio

FLUJO DE SUCESOS:
Camino Basico:

1. El administrador entra al panel principal.
2. El sistema identifica el negocio del administrador.
3. El sistema busca los proximos turnos de ese negocio.
4. El sistema muestra la lista de turnos.
5. De cada turno se muestra la fecha, hora de inicio, hora de fin, servicio, cliente y estado.
6. El administrador revisa los turnos pendientes del negocio.

Caminos Alternativos:

3.a El negocio no tiene proximos turnos.
3.a.1 El sistema muestra el mensaje "No tenes proximos turnos".

4.a Un turno ya esta cancelado.
4.a.1 El sistema no lo incluye entre los proximos turnos activos.

3.b No se pueden cargar los turnos.
3.b.1 El sistema muestra un mensaje de error.
