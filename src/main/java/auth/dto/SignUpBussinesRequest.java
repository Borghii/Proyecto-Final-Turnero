package auth.dto;

import shared.exception.ValidationException;

public class SignUpBussinesRequest {

    private SignUpClientRequest signUpClientRequest;

    private String nombre;
    private String ciudad;

    public SignUpBussinesRequest(SignUpClientRequest signUpClientRequest,String nombre, String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new ValidationException("La ciudad es obligatoria");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidationException("El nombre es obligatorio");
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
