package servicio.dto;

import shared.exception.ValidationException;

public class GuardarServicioRequest {
	private static final int MAX_NOMBRE_LENGTH = 150;
	private static final double MAX_PRECIO = 99999999.99;

	private final String nombre;
	private final double precio;
	private final int duracion;

	public GuardarServicioRequest(String nombre, String precio, String duracion) {
		this.nombre = validarNombre(nombre);
		this.precio = parsePrecio(precio);
		this.duracion = parseDuracion(duracion);
	}

	public String getNombre() {
		return nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public int getDuracion() {
		return duracion;
	}

	protected static String validarNombre(String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new ValidationException("El nombre del servicio es obligatorio");
		}

		String nombre = value.trim();
		if (nombre.length() > MAX_NOMBRE_LENGTH) {
			throw new ValidationException("El nombre del servicio no puede superar 150 caracteres");
		}

		return nombre;
	}

	protected static double parsePrecio(String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new ValidationException("El precio del servicio es obligatorio");
		}

		try {
			double precio = Double.parseDouble(value.trim());
			if (precio < 0 || precio > MAX_PRECIO) {
				throw new ValidationException("El precio del servicio no es valido");
			}

			return precio;
		} catch (NumberFormatException e) {
			throw new ValidationException("El precio del servicio no es valido");
		}
	}

	protected static int parseDuracion(String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new ValidationException("La duracion del servicio es obligatoria");
		}

		try {
			int duracion = Integer.parseInt(value.trim());
			if (duracion <= 0) {
				throw new ValidationException("La duracion del servicio no es valida");
			}

			return duracion;
		} catch (NumberFormatException e) {
			throw new ValidationException("La duracion del servicio no es valida");
		}
	}
}
