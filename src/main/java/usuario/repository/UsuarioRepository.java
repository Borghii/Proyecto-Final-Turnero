package usuario.repository;

import java.sql.SQLException;
import java.util.Optional;

import usuario.entity.Usuario;

public interface UsuarioRepository {
	Optional<Usuario> findByMail(String mail) throws SQLException;

	boolean existsByMail(String mail) throws SQLException;

	Usuario save(Usuario usuario) throws SQLException;
}
