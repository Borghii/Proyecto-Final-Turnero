# CU09 - Registrar cliente

FLUJO DE SUCESOS:
Camino Basico:

1. El usuario entra a la opcion "Registrarme como cliente".
2. El sistema muestra el formulario de registro de cliente.
3. El usuario ingresa nombre, mail y contrasena.
4. El usuario confirma el registro.
5. El sistema revisa que los datos esten completos.
6. El sistema revisa que el mail tenga un formato valido.
7. El sistema revisa que el mail no este registrado.
8. El sistema crea el usuario como cliente.
9. El sistema vuelve al login y muestra que el registro fue exitoso.

Caminos Alternativos:

5.a Falta algun dato obligatorio.
5.a.1 El sistema muestra un mensaje indicando el problema.
5.a.2 El usuario queda en el formulario para corregir los datos.

6.a El mail no tiene un formato valido.
6.a.1 El sistema muestra un mensaje indicando que el mail no es valido.
6.a.2 El usuario queda en el formulario para corregirlo.

7.a El mail ya esta registrado.
7.a.1 El sistema muestra un mensaje indicando que ya existe un usuario con ese mail.
7.a.2 El usuario queda en el formulario para cambiar el mail o iniciar sesion.
