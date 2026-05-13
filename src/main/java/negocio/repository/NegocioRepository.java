package negocio.repository;

import shared.db.ConexionDB;

public class NegocioRepository {
	private final ConexionDB conexionDB;

	public NegocioRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}
}
