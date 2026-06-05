package negocio.dto;

import shared.exception.ValidationException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActualizarNegocioRequest {
	private final String nombre;
	private final String ciudad;
	private final List<ActualizarHorarioRequest> horarios;

	public ActualizarNegocioRequest(String nombre, String ciudad, List<ActualizarHorarioRequest> horarios) {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new ValidationException("El nombre es obligatorio");
		}

		if (nombre.trim().length() > 150) {
			throw new ValidationException("El nombre no puede superar 150 caracteres");
		}

		if (ciudad == null || ciudad.trim().isEmpty()) {
			throw new ValidationException("La ciudad es obligatoria");
		}

		if (ciudad.trim().length() > 100) {
			throw new ValidationException("La ciudad no puede superar 100 caracteres");
		}

		if (horarios == null || horarios.size() != 7) {
			throw new ValidationException("Deben informarse los horarios de los 7 dias");
		}

		validarDiasSinRepetir(horarios);

		this.nombre = nombre.trim();
		this.ciudad = ciudad.trim();
		this.horarios = horarios;
	}

	public String getNombre() {
		return nombre;
	}

	public String getCiudad() {
		return ciudad;
	}

	public List<ActualizarHorarioRequest> getHorarios() {
		return horarios;
	}

	private void validarDiasSinRepetir(List<ActualizarHorarioRequest> horarios) {
		Set<Integer> dias = new HashSet<>();

		for (ActualizarHorarioRequest horario : horarios) {
			if (!dias.add(horario.getDia())) {
				throw new ValidationException("Hay dias repetidos en los horarios");
			}
		}
	}
}
