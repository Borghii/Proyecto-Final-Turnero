package auth.dto;

import shared.exception.ValidationException;

public class SignUpBussinesRequest {
    private static final int MAX_NOMBRE_LENGTH = 150;
    private static final int MAX_CIUDAD_LENGTH = 100;

    private SignUpClientRequest signUpClientRequest;

    private String nombre;
    private String ciudad;

    public SignUpBussinesRequest(SignUpClientRequest signUpClientRequest,String nombre, String ciudad) {
        if (signUpClientRequest == null) {
            throw new ValidationException("Los datos del usuario son obligatorios");
        }

        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new ValidationException("La ciudad es obligatoria");
        }

        if (ciudad.trim().length() > MAX_CIUDAD_LENGTH) {
            throw new ValidationException("La ciudad no puede superar 100 caracteres");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidationException("El nombre es obligatorio");
        }

        if (nombre.trim().length() > MAX_NOMBRE_LENGTH) {
            throw new ValidationException("El nombre no puede superar 150 caracteres");
        }

        this.nombre = nombre.trim();
        this.ciudad = ciudad.trim();
        this.signUpClientRequest = signUpClientRequest;
    }

    public String getNombre() {
        return nombre;
    }
    public String getCiudad() {
        return ciudad;
    }

    public SignUpClientRequest getSignUpClientRequest() {
        return signUpClientRequest;
    }
}
