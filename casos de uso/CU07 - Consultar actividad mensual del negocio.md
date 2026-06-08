# CU07 - Consultar actividad mensual del negocio

FLUJO DE SUCESOS:
Camino Basico:

1. El administrador entra a "Actividad".
2. El sistema identifica el negocio del administrador.
3. El sistema busca la actividad del mes actual.
4. El sistema calcula el total de turnos del mes.
5. El sistema calcula los turnos realizados.
6. El sistema calcula los turnos cancelados.
7. El sistema calcula el dinero generado.
8. El sistema muestra el resumen en pantalla.

Caminos Alternativos:

2.a El sistema no encuentra el negocio del administrador.
2.a.1 El sistema muestra un mensaje de error.

3.a No hay datos de actividad para mostrar.
3.a.1 El sistema muestra un mensaje indicando que no hay datos.

3.b No se puede cargar la actividad.
3.b.1 El sistema muestra un mensaje de error.
