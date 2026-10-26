package negocio.entity;

public class Negocio {
	private int idNegocio;
	private String ciudad;
	private String nombre;
	private int idUsuario;


	public Negocio(String ciudad, String nombre, int idUsuario) {
		this.ciudad = ciudad;
		this.nombre = nombre;
		this.idUsuario = idUsuario;
	}

	public Negocio(int idNegocio, String ciudad, String nombre, int idUsuario) {
		this.idNegocio = idNegocio;
		this.ciudad = ciudad;
		this.nombre = nombre;
		this.idUsuario = idUsuario;
	}

	public int getIdNegocio() {
		return idNegocio;
	}

	public String getCiudad() {
		return ciudad;
	}

	public String getNombre() {
		return nombre;
	}

	public int getIdUsuario() {
		return idUsuario;
	}
}
