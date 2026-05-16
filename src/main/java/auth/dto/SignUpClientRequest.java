package auth.dto;

import java.util.regex.Pattern;

import shared.exception.ValidationException;

public class SignUpClientRequest {

	private static final Pattern EMAIL_PATTERN = Pattern.compile(
			"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9]"
					+ "(?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?"
					+ "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");

	private final String mail;
	private final String password;
	private final String nombre;

	public SignUpClientRequest(String mail, String password, String nombre) {
		if (mail == null || mail.trim().isEmpty()) {
			throw new ValidationException("El mail es obligatorio");
		}

		if (!EMAIL_PATTERN.matcher(mail.trim()).matches()) {
			throw new ValidationException("El mail no tiene un formato valido");
		}

		if (password == null || password.trim().isEmpty()) {
			throw new ValidationException("La contrasena es obligatoria");
		}

		if (nombre == null || nombre.trim().isEmpty()) {
			throw new ValidationException("El nombre es obligatorio");
		}

		this.nombre = nombre.trim();
		this.mail = mail.trim().toLowerCase();
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public String getPassword() {
		return password;
	}

	public String getNombre() {
		return nombre;
	}
}
