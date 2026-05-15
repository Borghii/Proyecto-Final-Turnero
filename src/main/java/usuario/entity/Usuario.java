package usuario.entity;

import shared.enums.RolEnum;

public class Usuario {
	private int idUsuario;
	private String mail;
	private String pass;
	private String nombre;
	private RolEnum role;

	public Usuario(int idUsuario, String mail, String pass, String nombre, RolEnum role) {
		this.idUsuario = idUsuario;
		this.mail = mail;
		this.pass = pass;
		this.nombre = nombre;
		this.role = role;
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

	public RolEnum getRole() {
		return role;
	}
}
