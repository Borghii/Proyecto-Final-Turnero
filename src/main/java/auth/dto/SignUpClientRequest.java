package auth.dto;

import java.util.regex.Pattern;

import shared.exception.ValidationException;

public class SignUpClientRequest {
	private static final int MAX_MAIL_LENGTH = 150;
	private static final int MAX_PASSWORD_LENGTH = 100;
	private static final int MAX_NOMBRE_LENGTH = 100;

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

		String mailNormalizado = mail.trim().toLowerCase();
		if (mailNormalizado.length() > MAX_MAIL_LENGTH) {
			throw new ValidationException("El mail no puede superar 150 caracteres");
		}

		if (!EMAIL_PATTERN.matcher(mailNormalizado).matches()) {
			throw new ValidationException("El mail no tiene un formato valido");
		}

		if (password == null || password.trim().isEmpty()) {
			throw new ValidationException("La contrasena es obligatoria");
		}

		if (password.length() > MAX_PASSWORD_LENGTH) {
			throw new ValidationException("La contrasena no puede superar 100 caracteres");
		}

		if (nombre == null || nombre.trim().isEmpty()) {
			throw new ValidationException("El nombre es obligatorio");
		}

		if (nombre.trim().length() > MAX_NOMBRE_LENGTH) {
			throw new ValidationException("El nombre no puede superar 100 caracteres");
		}

		this.nombre = nombre.trim();
		this.mail = mailNormalizado;
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
