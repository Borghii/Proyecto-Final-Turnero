# CU05 - Actualizar datos y horarios del negocio

FLUJO DE SUCESOS:
Camino Basico:

1. El administrador entra a "Mi negocio".
2. El sistema muestra el nombre, la ciudad y los horarios actuales del negocio.
3. El administrador modifica los datos generales del negocio.
4. El administrador modifica los dias activos y las franjas horarias.
5. El administrador guarda los cambios.
6. El sistema revisa que el nombre y la ciudad esten completos.
7. El sistema revisa que esten informados los horarios de los 7 dias.
8. El sistema revisa que las franjas horarias sean correctas.
9. El sistema revisa que los turnos proximos sigan quedando dentro del nuevo horario.
10. El sistema guarda los cambios.
11. El sistema muestra un mensaje indicando que los cambios se guardaron correctamente.

Caminos Alternativos:

2.a El sistema no encuentra el negocio del administrador.
2.a.1 El sistema muestra un mensaje de error.

6.a Falta el nombre o la ciudad.
6.a.1 El sistema muestra un mensaje indicando el dato faltante.
6.a.2 El administrador queda en la pantalla para corregirlo.

7.a No estan cargados los 7 dias.
7.a.1 El sistema muestra un mensaje indicando que faltan horarios.
7.a.2 Los cambios no se guardan.

8.a Una franja horaria esta incompleta.
8.a.1 El sistema muestra un mensaje indicando que falta una hora desde o una hora hasta.
8.a.2 Los cambios no se guardan.

8.b Una hora de fin es anterior o igual a la hora de inicio.
8.b.1 El sistema muestra un mensaje indicando que el horario no es valido.
8.b.2 Los cambios no se guardan.

9.a Hay turnos proximos que quedan fuera del nuevo horario.
9.a.1 El sistema muestra un mensaje indicando que primero debe cancelar esos turnos.
9.a.2 Los cambios no se guardan.
