package usuario.repository;

import shared.db.ConexionDB;

public class UsuarioRepository {
	private final ConexionDB conexionDB;

	public UsuarioRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}
}
