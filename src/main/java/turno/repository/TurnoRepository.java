package turno.repository;

import shared.db.ConexionDB;

public class TurnoRepository {
	private final ConexionDB conexionDB;

	public TurnoRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}
}
