# CU02 - Iniciar sesion

FLUJO DE SUCESOS:
Camino Basico:

1. El usuario entra a la pantalla de login.
2. El sistema muestra los campos de mail y contrasena.
3. El usuario ingresa su mail y su contrasena.
4. El usuario confirma el ingreso.
5. El sistema revisa que los datos esten completos.
6. El sistema busca el usuario por mail.
7. El sistema valida que la contrasena sea correcta.
8. El sistema inicia la sesion del usuario.
9. Si el usuario es administrador, el sistema lo lleva al panel de administrador.
10. Si el usuario es cliente, el sistema lo lleva al panel de cliente.

Caminos Alternativos:

1.a El usuario ya tiene una sesion iniciada.
1.a.1 El sistema lo manda directamente al panel que corresponde.

5.a Falta el mail o la contrasena.
5.a.1 El sistema muestra un mensaje indicando el dato faltante.
5.a.2 El usuario queda en la pantalla de login.

6.a No existe un usuario con ese mail.
6.a.1 El sistema muestra un mensaje indicando que no encontro el usuario.
6.a.2 El usuario queda en la pantalla de login.

7.a La contrasena es incorrecta.
7.a.1 El sistema muestra un mensaje indicando que la contrasena no es correcta.
7.a.2 El usuario queda en la pantalla de login.
