package servicio.dto;

import shared.exception.ValidationException;

public class BuscarServiciosRequest {
	private final int idNegocio;

	public BuscarServiciosRequest(String idNegocio) {
		this.idNegocio = parseIdNegocio(idNegocio);
	}

	public int getIdNegocio() {
		return idNegocio;
	}

	private static int parseIdNegocio(String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new ValidationException("El negocio indicado no es valido");
		}

		try {
			int parsedValue = Integer.parseInt(value.trim());
			if (parsedValue <= 0) {
				throw new ValidationException("El negocio indicado no es valido");
			}
			return parsedValue;
		} catch (NumberFormatException e) {
			throw new ValidationException("El negocio indicado no es valido");
		}
	}
}
