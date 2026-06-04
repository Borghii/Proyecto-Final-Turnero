package turno.dto;

import shared.exception.ValidationException;

public class SeleccionarNegocioRequest {
	private final int idNegocio;

	public SeleccionarNegocioRequest(String idNegocio) {
		this.idNegocio = parsePositiveInt(idNegocio, "El negocio indicado no es valido");
	}

	public int getIdNegocio() {
		return idNegocio;
	}

	private static int parsePositiveInt(String value, String message) {
		if (value == null || value.trim().isEmpty()) {
			throw new ValidationException(message);
		}

		try {
			int parsedValue = Integer.parseInt(value.trim());
			if (parsedValue <= 0) {
				throw new ValidationException(message);
			}
			return parsedValue;
		} catch (NumberFormatException e) {
			throw new ValidationException(message);
		}
	}
}
