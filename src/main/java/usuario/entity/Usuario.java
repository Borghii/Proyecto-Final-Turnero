package usuario.entity;

public class Usuario {
	private int idUsuario;
	private String mail;
	private String pass;
	private String nombre;
	private int idRol;

	public Usuario(int idUsuario, String mail, String pass, String nombre, int idRol) {
		this.idUsuario = idUsuario;
		this.mail = mail;
		this.pass = pass;
		this.nombre = nombre;
		this.idRol = idRol;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public String getMail() {
		return mail;
	}

	public String getPass() {
		return pass;
	}

	public String getNombre() {
		return nombre;
	}

	public int getIdRol() {
		return idRol;
	}
}
