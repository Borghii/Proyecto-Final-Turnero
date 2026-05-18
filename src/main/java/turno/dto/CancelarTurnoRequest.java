package turno.dto;

import shared.exception.ValidationException;

public class CancelarTurnoRequest {
	private final int idTurno;

	public CancelarTurnoRequest(String idTurno) {
		if (idTurno == null || idTurno.trim().isEmpty()) {
			throw new ValidationException("El turno indicado es obligatorio");
		}

		try {
			this.idTurno = Integer.parseInt(idTurno.trim());
		} catch (NumberFormatException e) {
			throw new ValidationException("El turno indicado no es valido");
		}

		if (this.idTurno <= 0) {
			throw new ValidationException("El turno indicado no es valido");
		}
	}

	public int getIdTurno() {
		return idTurno;
	}
}
