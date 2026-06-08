# CU01 - Registrar negocio

FLUJO DE SUCESOS:
Camino Basico:

1. El usuario entra a la opcion "Registrar mi negocio".
2. El sistema muestra el formulario para cargar los datos del administrador y del negocio.
3. El usuario ingresa su nombre, mail, contrasena, nombre del negocio y ciudad.
4. El usuario confirma el registro.
5. El sistema revisa que los datos esten completos y que el mail tenga un formato valido.
6. El sistema crea el usuario como administrador.
7. El sistema registra el negocio asociado a ese administrador.
8. El sistema carga horarios iniciales para el negocio.
9. El sistema vuelve al login y muestra que el registro fue exitoso.

Caminos Alternativos:

5.a Falta algun dato obligatorio.
5.a.1 El sistema muestra un mensaje indicando el problema.
5.a.2 El usuario queda en el formulario para corregir los datos.

5.b El mail no tiene un formato valido.
5.b.1 El sistema muestra un mensaje indicando que el mail no es valido.
5.b.2 El usuario queda en el formulario para corregirlo.

6.a El mail ya esta registrado.
6.a.1 El sistema muestra un mensaje indicando que ya existe un usuario con ese mail.
6.a.2 El usuario queda en el formulario para cambiar el mail o iniciar sesion.

7.a No se puede registrar el negocio.
7.a.1 El sistema muestra un mensaje de error.
7.a.2 El registro no queda completado.
