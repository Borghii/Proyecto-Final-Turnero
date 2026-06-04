package turno.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import shared.exception.ValidationException;

public class ConfirmarTurnoRequest {
	private final int idNegocio;
	private final int idServicio;
	private final LocalDate fecha;
	private final LocalTime hora;

	public ConfirmarTurnoRequest(String idNegocio, String idServicio, String fecha, String hora) {
		this.idNegocio = parsePositiveInt(idNegocio, "El negocio indicado no es valido");
		this.idServicio = parsePositiveInt(idServicio, "El servicio indicado no es valido");
		this.fecha = parseFecha(fecha);
		this.hora = parseHora(hora);
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

	public LocalTime getHora() {
		return hora;
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
		if (value == null || value.trim().isEmpty()) {
			throw new ValidationException("La fecha indicada es obligatoria");
		}

		LocalDate fecha;
		try {
			fecha = LocalDate.parse(value.trim());
		} catch (DateTimeParseException e) {
			throw new ValidationException("La fecha indicada no es valida");
		}

		if (fecha.isBefore(LocalDate.now())) {
			throw new ValidationException("La fecha indicada no puede ser anterior a hoy");
		}

		return fecha;
	}

	private static LocalTime parseHora(String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new ValidationException("La hora indicada es obligatoria");
		}

		try {
			return LocalTime.parse(value.trim());
		} catch (DateTimeParseException e) {
			throw new ValidationException("La hora indicada no es valida");
		}
	}
}
