package usuario.service;

import java.sql.SQLException;

import usuario.entity.Usuario;

public interface UsuarioService {
	Usuario findByMail(String mail) throws SQLException;
}
