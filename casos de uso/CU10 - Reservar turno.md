# CU10 - Reservar turno

FLUJO DE SUCESOS:
Camino Basico:

1. El cliente entra a su panel.
2. El sistema muestra los negocios disponibles.
3. El cliente elige un negocio.
4. El sistema muestra los servicios de ese negocio.
5. El cliente elige un servicio.
6. El sistema muestra la pantalla para elegir fecha y hora.
7. El cliente selecciona una fecha.
8. El sistema busca los horarios disponibles para esa fecha y ese servicio.
9. El cliente selecciona un horario.
10. El sistema muestra el resumen del turno con servicio, fecha, hora, duracion y precio.
11. El cliente confirma la reserva.
12. El sistema revisa que el horario siga disponible.
13. El sistema revisa que el cliente no tenga otro turno que se cruce con ese horario.
14. El sistema registra el turno como confirmado.
15. El sistema lleva al cliente a "Mis turnos".

Caminos Alternativos:

2.a No hay negocios registrados.
2.a.1 El sistema muestra un mensaje indicando que no hay negocios disponibles.

4.a El negocio no tiene servicios cargados.
4.a.1 El sistema muestra un mensaje indicando que no hay servicios.
4.a.2 El cliente puede volver al panel.

7.a La fecha indicada es anterior a hoy.
7.a.1 El sistema muestra un mensaje indicando que la fecha no puede ser anterior a hoy.
7.a.2 El cliente debe elegir otra fecha.

8.a No hay horarios disponibles para esa fecha.
8.a.1 El sistema muestra un mensaje indicando que no hay turnos disponibles.
8.a.2 El cliente puede buscar otro dia.

12.a El horario ya no esta disponible.
12.a.1 El sistema muestra un mensaje indicando que el horario seleccionado ya no esta disponible.
12.a.2 El cliente vuelve a elegir un horario.

13.a El cliente ya tiene un turno que se cruza con ese horario.
13.a.1 El sistema muestra un mensaje indicando que ya tiene un turno reservado en ese horario.
13.a.2 El turno no se registra.

4.b El servicio seleccionado no pertenece al negocio.
4.b.1 El sistema muestra un mensaje de error.
4.b.2 El turno no se registra.
