package turno.dto;

import shared.enums.EstadoTurnoEnum;

public class TurnoParaCancelacion {
	private final int idTurno;
	private final int idUsuarioDuenioNegocio;
	private final EstadoTurnoEnum estado;

	public TurnoParaCancelacion(int idTurno, int idUsuarioDuenioNegocio, EstadoTurnoEnum estado) {
		this.idTurno = idTurno;
		this.idUsuarioDuenioNegocio = idUsuarioDuenioNegocio;
		this.estado = estado;
	}

	public int getIdTurno() {
		return idTurno;
	}

	public int getIdUsuarioDuenioNegocio() {
		return idUsuarioDuenioNegocio;
	}

	public EstadoTurnoEnum getEstado() {
		return estado;
	}
}
