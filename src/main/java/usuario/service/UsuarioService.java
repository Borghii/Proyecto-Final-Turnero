package usuario.service;

import java.sql.SQLException;

import shared.exception.ResourceNotFoundException;
import usuario.entity.Usuario;
import usuario.repository.UsuarioRepository;

public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public Usuario findByMail(String mail) throws SQLException {
		return usuarioRepository.findByMail(mail)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No existe un usuario registrado con el mail " + mail));
	}
}
