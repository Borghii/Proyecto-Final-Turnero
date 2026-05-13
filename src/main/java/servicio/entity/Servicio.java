package servicio.entity;

public class Servicio {
	private int idServicio;
	private String nombre;
	private double precio;
	private int duracion;
	private int idNegocio;

	public Servicio(int idServicio, String nombre, double precio, int duracion, int idNegocio) {
		this.idServicio = idServicio;
		this.nombre = nombre;
		this.precio = precio;
		this.duracion = duracion;
		this.idNegocio = idNegocio;
	}

	public int getIdServicio() {
		return idServicio;
	}

	public String getNombre() {
		return nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public int getDuracion() {
		return duracion;
	}

	public int getIdNegocio() {
		return idNegocio;
	}
}
