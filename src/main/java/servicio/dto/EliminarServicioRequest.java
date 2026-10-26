package servicio.dto;

import shared.exception.ValidationException;

public class EliminarServicioRequest {
	private final int idServicio;

	public EliminarServicioRequest(String idServicio) {
		this.idServicio = parseIdServicio(idServicio);
	}

	public int getIdServicio() {
		return idServicio;
	}

	private static int parseIdServicio(String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new ValidationException("El servicio indicado no es valido");
		}

		try {
			int parsedValue = Integer.parseInt(value.trim());
			if (parsedValue <= 0) {
				throw new ValidationException("El servicio indicado no es valido");
			}

			return parsedValue;
		} catch (NumberFormatException e) {
			throw new ValidationException("El servicio indicado no es valido");
		}
	}
}
