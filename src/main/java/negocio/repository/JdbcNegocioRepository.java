package negocio.repository;

import shared.db.ConexionDB;

public class JdbcNegocioRepository implements NegocioRepository {
	private final ConexionDB conexionDB;

	public JdbcNegocioRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}
}
