package turno.dto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import shared.exception.ValidationException;

public class CalendarioTurnoRequest {
	private final int idNegocio;
	private final int idServicio;
	private final LocalDate fecha;

	public CalendarioTurnoRequest(String idNegocio, String idServicio, String fecha) {
		this.idNegocio = parseIdNegocio(idNegocio);
		this.idServicio = parseIdServicio(idServicio);
		this.fecha = parseFecha(fecha);
	}

	public int getIdNegocio() {
		return idNegocio;
	}

	public int getIdServicio() {
		return idServicio;
	}

	public LocalDate getFecha() {
		return fecha;
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

	private static LocalDate parseFecha(String value) {
		LocalDate fecha = LocalDate.now();

		if (value != null && !value.trim().isEmpty()) {
			try {
				fecha = LocalDate.parse(value.trim());
			} catch (DateTimeParseException e) {
				throw new ValidationException("La fecha indicada no es valida");
			}
		}

		if (fecha.isBefore(LocalDate.now())) {
			throw new ValidationException("La fecha indicada no puede ser anterior a hoy");
		}

		return fecha;
	}
}
