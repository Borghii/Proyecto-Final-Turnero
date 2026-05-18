package turno.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import shared.enums.EstadoTurnoEnum;

public class TurnoConServicio {
	private int idTurno;
	private int idServicio;
	private String nombreServicio;
	private int idUsuario;
	private String mailCliente;
	private String nombreCliente;
	private LocalDate fecha;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private EstadoTurnoEnum estado;

	public TurnoConServicio(int idTurno, int idServicio, String nombreServicio, int idUsuario, String mailCliente,
			String nombreCliente, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, EstadoTurnoEnum estado) {
		this.idTurno = idTurno;
		this.idServicio = idServicio;
		this.nombreServicio = nombreServicio;
		this.idUsuario = idUsuario;
		this.mailCliente = mailCliente;
		this.nombreCliente = nombreCliente;
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

	public String getNombreServicio() {
		return nombreServicio;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public String getMailCliente() {
		return mailCliente;
	}

	public String getNombreCliente() {
		return nombreCliente;
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
