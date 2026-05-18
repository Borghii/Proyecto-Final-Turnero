package turno.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import shared.enums.EstadoTurnoEnum;

public class Turno {
	private int idTurno;
	private int idServicio;
	private int idUsuario;
	private LocalDate fecha;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private EstadoTurnoEnum estado;

	public Turno(int idTurno, int idServicio, int idUsuario, LocalDate fecha,
			LocalTime horaInicio, LocalTime horaFin, EstadoTurnoEnum estado) {
		this.idTurno = idTurno;
		this.idServicio = idServicio;
		this.idUsuario = idUsuario;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.estado = estado;
	}

	public int getIdTurno() {
		return idTurno;
	}

	public int getIdServicio() {
		return idServicio;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public EstadoTurnoEnum getEstado() {
		return estado;
	}
}
