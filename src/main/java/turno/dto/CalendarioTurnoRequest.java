package turno.dto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import shared.exception.ValidationException;

public class CalendarioTurnoRequest {
	private final int idNegocio;
	private final int idServicio;
	private final LocalDate fecha;

	public CalendarioTurnoRequest(String idNegocio, String idServicio, String fecha) {
		this.idNegocio = parsePositiveInt(idNegocio, "El negocio indicado no es valido");
		this.idServicio = parsePositiveInt(idServicio, "El servicio indicado no es valido");
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
