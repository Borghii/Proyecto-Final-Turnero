package servicio.repository;

import shared.db.ConexionDB;

public class JdbcServicioRepository implements ServicioRepository {
	private final ConexionDB conexionDB;

	public JdbcServicioRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}
}
