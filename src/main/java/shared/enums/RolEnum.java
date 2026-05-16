package shared.enums;

public enum RolEnum {
	CLIENTE(1),
	ADMIN(2);

	private final int idRol;

	RolEnum(int idRol) {
		this.idRol = idRol;
	}

	public int getIdRol() {
		return idRol;
	}
}
