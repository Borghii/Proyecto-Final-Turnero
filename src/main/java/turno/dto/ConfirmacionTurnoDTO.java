package turno.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConfirmacionTurnoDTO {
	private final int idNegocio;
	private final int idServicio;
	private final String nombreServicio;
	private final double precio;
	private final int duracion;
	private final LocalDate fecha;
	private final LocalTime horaInicio;
	private final LocalTime horaFin;

	public ConfirmacionTurnoDTO(int idNegocio, int idServicio, String nombreServicio, double precio, int duracion,
			LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
		this.idNegocio = idNegocio;
		this.idServicio = idServicio;
		this.nombreServicio = nombreServicio;
		this.precio = precio;
		this.duracion = duracion;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}

	public int getIdNegocio() {
		return idNegocio;
	}

	public int getIdServicio() {
		return idServicio;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public double getPrecio() {
		return precio;
	}

	public int getDuracion() {
		return duracion;
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
}
