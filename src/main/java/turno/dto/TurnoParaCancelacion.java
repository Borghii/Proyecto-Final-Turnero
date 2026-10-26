package turno.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import shared.enums.EstadoTurnoEnum;

public class TurnoParaCancelacion {
	private final int idTurno;
	private final int idUsuarioCliente;
	private final int idUsuarioDuenioNegocio;
	private final LocalDate fecha;
	private final LocalTime horaInicio;
	private final EstadoTurnoEnum estado;

	public TurnoParaCancelacion(int idTurno, int idUsuarioCliente, int idUsuarioDuenioNegocio,
			LocalDate fecha, LocalTime horaInicio, EstadoTurnoEnum estado) {
		this.idTurno = idTurno;
		this.idUsuarioCliente = idUsuarioCliente;
		this.idUsuarioDuenioNegocio = idUsuarioDuenioNegocio;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
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

	public LocalDate getFecha() {
		return fecha;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public EstadoTurnoEnum getEstado() {
		return estado;
	}
}
