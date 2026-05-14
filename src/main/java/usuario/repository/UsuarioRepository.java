package usuario.repository;

import java.sql.SQLException;
import java.util.Optional;

import usuario.entity.Usuario;

public interface UsuarioRepository {
	Optional<Usuario> findByMail(String mail) throws SQLException;

}
