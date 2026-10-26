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

	public Usuario(String mail, String pass, String nombre) {
		this.mail = mail;
		this.pass = pass;
		this.nombre = nombre;
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

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setRole(RolEnum role) {
		this.role = role;
	}
}
