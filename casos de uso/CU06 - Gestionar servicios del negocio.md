# CU06 - Gestionar servicios del negocio

FLUJO DE SUCESOS:
Camino Basico:

1. El administrador entra a "Mis servicios".
2. El sistema muestra los servicios actuales del negocio.
3. El sistema muestra el formulario para agregar un servicio nuevo.
4. El administrador carga nombre, precio y duracion del servicio.
5. El administrador guarda el servicio.
6. El sistema revisa que los datos sean validos.
7. El sistema guarda el nuevo servicio.
8. El administrador puede modificar un servicio existente.
9. El sistema revisa que el servicio pertenezca a su negocio.
10. El sistema guarda los cambios del servicio.
11. El administrador puede borrar un servicio existente.
12. El sistema revisa que el servicio pertenezca a su negocio y que no tenga turnos asociados.
13. El sistema borra el servicio.
14. El sistema vuelve a mostrar la lista actualizada.

Caminos Alternativos:

2.a El negocio no tiene servicios cargados.
2.a.1 El sistema muestra el mensaje "No tenes servicios cargados".

6.a Falta el nombre, el precio o la duracion.
6.a.1 El sistema muestra un mensaje indicando el problema.
6.a.2 El servicio no se guarda.

6.b La duracion no es multiplo de 30 minutos.
6.b.1 El sistema muestra un mensaje indicando que la duracion debe ser 30, 60, 90, etc.
6.b.2 El servicio no se guarda.

9.a El servicio no existe.
9.a.1 El sistema muestra un mensaje indicando que no encontro el servicio.
9.a.2 Los cambios no se guardan.

9.b El servicio no pertenece al negocio del administrador.
9.b.1 El sistema muestra un mensaje indicando que no tiene permiso.
9.b.2 Los cambios no se guardan.

12.a El servicio tiene turnos asociados.
12.a.1 El sistema muestra un mensaje indicando que no puede eliminarlo.
12.a.2 El servicio queda cargado.
