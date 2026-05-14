package auth.dto;

import shared.exception.ValidationException;

public class LoginRequest {
	private final String mail;
	private final String password;

	public LoginRequest(String mail, String password) {
		if (mail == null || mail.trim().isEmpty()) {
			throw new ValidationException("El mail es obligatorio");
		}

		if (password == null || password.trim().isEmpty()) {
			throw new ValidationException("La contrasena es obligatoria");
		}

		this.mail = mail.trim().toLowerCase();
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public String getPassword() {
		return password;
	}
}
