package turno.repository;

import shared.db.ConexionDB;

public class JdbcTurnoRepository implements TurnoRepository {
	private final ConexionDB conexionDB;

	public JdbcTurnoRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}
}
