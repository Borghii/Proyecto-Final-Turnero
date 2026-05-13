package servicio.repository;

import shared.db.ConexionDB;

public class ServicioRepository {
	private final ConexionDB conexionDB;

	public ServicioRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}
}
