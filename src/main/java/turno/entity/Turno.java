package turno.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {
	private int idTurno;
	private int idServicio;
	private int idUsuario;
	private LocalDate fecha;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private String estado;

	public Turno(int idTurno, int idServicio, int idUsuario, LocalDate fecha,
			LocalTime horaInicio, LocalTime horaFin, String estado) {
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

	public String getEstado() {
		return estado;
	}
}
