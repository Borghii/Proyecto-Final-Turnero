package turno.dto;

import shared.enums.EstadoTurnoEnum;

public class TurnoParaCancelacion {
	private final int idTurno;
	private final int idUsuarioCliente;
	private final int idUsuarioDuenioNegocio;
	private final EstadoTurnoEnum estado;

	public TurnoParaCancelacion(int idTurno, int idUsuarioCliente, int idUsuarioDuenioNegocio,
			EstadoTurnoEnum estado) {
		this.idTurno = idTurno;
		this.idUsuarioCliente = idUsuarioCliente;
		this.idUsuarioDuenioNegocio = idUsuarioDuenioNegocio;
		this.estado = estado;
	}

	public int getIdTurno() {
		return idTurno;
	}

	public int getIdUsuarioCliente() {
		return idUsuarioCliente;
	}

	public int getIdUsuarioDuenioNegocio() {
		return idUsuarioDuenioNegocio;
	}

	public EstadoTurnoEnum getEstado() {
		return estado;
	}
}
