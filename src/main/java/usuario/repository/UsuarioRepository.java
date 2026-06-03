package usuario.repository;

import java.util.Optional;

import usuario.entity.Usuario;

public interface UsuarioRepository {
	Optional<Usuario> findByMail(String mail);

	boolean existsByMail(String mail);

	Usuario save(Usuario usuario);
}
