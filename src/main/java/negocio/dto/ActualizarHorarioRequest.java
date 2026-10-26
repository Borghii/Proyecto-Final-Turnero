package negocio.dto;

import shared.exception.ValidationException;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ActualizarHorarioRequest {
	private final int dia;
	private final boolean activo;
	private final LocalTime mananaDesde;
	private final LocalTime mananaHasta;
	private final LocalTime tardeDesde;
	private final LocalTime tardeHasta;

	public ActualizarHorarioRequest(String dia, boolean activo, String mananaDesde, String mananaHasta,
			String tardeDesde, String tardeHasta) {
		this.dia = parseDia(dia);
		this.activo = activo;
		this.mananaDesde = parseMananaDesde(mananaDesde);
		this.mananaHasta = parseMananaHasta(mananaHasta);
		this.tardeDesde = parseTardeDesde(tardeDesde);
		this.tardeHasta = parseTardeHasta(tardeHasta);

		validarRangos();
	}

	public int getDia() {
		return dia;
	}

	public boolean isActivo() {
		return activo;
	}

	public LocalTime getMananaDesde() {
		return mananaDesde;
	}

	public LocalTime getMananaHasta() {
		return mananaHasta;
	}

	public LocalTime getTardeDesde() {
		return tardeDesde;
	}

	public LocalTime getTardeHasta() {
		return tardeHasta;
	}

	private int parseDia(String dia) {
		if (dia == null || dia.trim().isEmpty()) {
			throw new ValidationException("El dia es obligatorio");
		}

		try {
			int valor = Integer.parseInt(dia.trim());
			if (valor < 1 || valor > 7) {
				throw new ValidationException("El dia no es valido");
			}
			return valor;
		} catch (NumberFormatException e) {
			throw new ValidationException("El dia no es valido");
		}
	}

	private LocalTime parseMananaDesde(String hora) {
		if (hora == null || hora.trim().isEmpty()) {
			return null;
		}

		try {
			LocalTime valor = LocalTime.parse(hora.trim());

			if (!esHorarioCada30Minutos(valor)) {
				throw new ValidationException("El horario de manana desde debe ser cada 30 minutos");
			}

			return valor;
		} catch (DateTimeParseException e) {
			throw new ValidationException("El horario de manana desde no es valido");
		}
	}

	private LocalTime parseMananaHasta(String hora) {
		if (hora == null || hora.trim().isEmpty()) {
			return null;
		}

		try {
			LocalTime valor = LocalTime.parse(hora.trim());

			if (!esHorarioCada30Minutos(valor)) {
				throw new ValidationException("El horario de manana hasta debe ser cada 30 minutos");
			}

			return valor;
		} catch (DateTimeParseException e) {
			throw new ValidationException("El horario de manana hasta no es valido");
		}
	}

	private LocalTime parseTardeDesde(String hora) {
		if (hora == null || hora.trim().isEmpty()) {
			return null;
		}

		try {
			LocalTime valor = LocalTime.parse(hora.trim());

			if (!esHorarioCada30Minutos(valor)) {
				throw new ValidationException("El horario de tarde desde debe ser cada 30 minutos");
			}

			return valor;
		} catch (DateTimeParseException e) {
			throw new ValidationException("El horario de tarde desde no es valido");
		}
	}

	private LocalTime parseTardeHasta(String hora) {
		if (hora == null || hora.trim().isEmpty()) {
			return null;
		}

		try {
			LocalTime valor = LocalTime.parse(hora.trim());

			if (!esHorarioCada30Minutos(valor)) {
				throw new ValidationException("El horario de tarde hasta debe ser cada 30 minutos");
			}

			return valor;
		} catch (DateTimeParseException e) {
			throw new ValidationException("El horario de tarde hasta no es valido");
		}
	}

	private boolean esHorarioCada30Minutos(LocalTime valor) {
		return (valor.getMinute() == 0 || valor.getMinute() == 30)
				&& valor.getSecond() == 0
				&& valor.getNano() == 0;
	}

	private void validarRangos() {
		if (mananaDesde != null && mananaDesde.isAfter(LocalTime.of(12, 0))) {
			throw new ValidationException("El horario de manana desde debe estar entre 00:00 y 12:00");
		}
		if (mananaHasta != null && mananaHasta.isAfter(LocalTime.of(12, 0))) {
			throw new ValidationException("El horario de manana hasta debe estar entre 00:00 y 12:00");
		}
		if (tardeDesde != null && (tardeDesde.isBefore(LocalTime.of(12, 0)) || tardeDesde.isAfter(LocalTime.of(23, 30)))) {
			throw new ValidationException("El horario de tarde desde debe estar entre 12:00 y 23:30");
		}
		if (tardeHasta != null && (tardeHasta.isBefore(LocalTime.of(12, 0)) || tardeHasta.isAfter(LocalTime.of(23, 30)))) {
			throw new ValidationException("El horario de tarde hasta debe estar entre 12:00 y 23:30");
		}
	}
}
