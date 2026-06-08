# CU04 - Cancelar turno del negocio

FLUJO DE SUCESOS:
Camino Basico:

1. El administrador entra al panel principal.
2. El sistema muestra los proximos turnos del negocio.
3. El administrador elige cancelar un turno.
4. El sistema revisa que el turno exista.
5. El sistema revisa que el turno pertenezca al negocio del administrador.
6. El sistema revisa que el turno no este cancelado.
7. El sistema marca el turno como cancelado.
8. El sistema vuelve al panel principal.
9. El turno queda visible con estado cancelado.

Caminos Alternativos:

4.a El turno indicado no es valido.
4.a.1 El sistema muestra un mensaje de error.
4.a.2 El turno no se cancela.

4.b El turno no existe.
4.b.1 El sistema muestra un mensaje indicando que no encontro el turno.
4.b.2 El turno no se cancela.

5.a El turno no pertenece al negocio del administrador.
5.a.1 El sistema muestra un mensaje indicando que no tiene permiso para cancelarlo.
5.a.2 El turno no se cancela.

6.a El turno ya estaba cancelado.
6.a.1 El sistema muestra un mensaje indicando que el turno ya esta cancelado.
6.a.2 El turno queda igual.
