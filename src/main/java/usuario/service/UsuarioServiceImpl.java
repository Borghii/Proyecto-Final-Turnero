package usuario.service;

import java.sql.SQLException;

import shared.exception.ResourceNotFoundException;
import usuario.entity.Usuario;
import usuario.repository.UsuarioRepository;

public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Usuario findByMail(String mail) throws SQLException {
		return usuarioRepository.findByMail(mail)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No existe un usuario registrado con el mail " + mail));
	}
}
