package servicio.dto;

import shared.exception.ValidationException;

public class ActualizarServicioRequest {
	private final int idServicio;
	private final String nombre;
	private final double precio;
	private final int duracion;

	public ActualizarServicioRequest(String idServicio, String nombre, String precio, String duracion) {
		this.idServicio = parseIdServicio(idServicio);
		this.nombre = GuardarServicioRequest.validarNombre(nombre);
		this.precio = GuardarServicioRequest.parsePrecio(precio);
		this.duracion = GuardarServicioRequest.parseDuracion(duracion);
	}

	public int getIdServicio() {
		return idServicio;
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
