package negocio.entity;

import java.time.LocalTime;

public class Horario {
	private int idHorario;
	private int idNegocio;
	private int dia;
	private LocalTime mananaDesde;
	private LocalTime mananaHasta;
	private LocalTime tardeDesde;
	private LocalTime tardeHasta;
	private boolean activo;

	public Horario(int dia, LocalTime mananaDesde, LocalTime mananaHasta,
			LocalTime tardeDesde, LocalTime tardeHasta, boolean activo) {
		this.dia = dia;
		this.mananaDesde = mananaDesde;
		this.mananaHasta = mananaHasta;
		this.tardeDesde = tardeDesde;
		this.tardeHasta = tardeHasta;
		this.activo = activo;
	}

	public Horario(int idNegocio, int dia, LocalTime mananaDesde, LocalTime mananaHasta,
			LocalTime tardeDesde, LocalTime tardeHasta, boolean activo) {
		this.idNegocio = idNegocio;
		this.dia = dia;
		this.mananaDesde = mananaDesde;
		this.mananaHasta = mananaHasta;
		this.tardeDesde = tardeDesde;
		this.tardeHasta = tardeHasta;
		this.activo = activo;
	}

	public Horario(int idHorario, int idNegocio, int dia, LocalTime mananaDesde,
			LocalTime mananaHasta, LocalTime tardeDesde, LocalTime tardeHasta,
			boolean activo) {
		this.idHorario = idHorario;
		this.idNegocio = idNegocio;
		this.dia = dia;
		this.mananaDesde = mananaDesde;
		this.mananaHasta = mananaHasta;
		this.tardeDesde = tardeDesde;
		this.tardeHasta = tardeHasta;
		this.activo = activo;
	}

	public int getIdHorario() {
		return idHorario;
	}

	public int getIdNegocio() {
		return idNegocio;
	}

	public int getDia() {
		return dia;
	}

	public LocalTime getMananaDesde() {
		return mananaDesde;
	}

	public LocalTime getMananaHasta() {
		return mananaHasta;
	}

	public LocalTime getTardeDesde() {
		return tardeDesde;
	}

	public LocalTime getTardeHasta() {
		return tardeHasta;
	}

	public boolean isActivo() {
		return activo;
	}

}
