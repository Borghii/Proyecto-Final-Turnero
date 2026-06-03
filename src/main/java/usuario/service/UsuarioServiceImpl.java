package usuario.service;

import shared.enums.RolEnum;
import shared.exception.ResourceNotFoundException;
import shared.exception.ValidationException;
import usuario.entity.Usuario;
import usuario.repository.UsuarioRepository;

public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Usuario findByMail(String mail) {
		return usuarioRepository.findByMail(mail)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No existe un usuario registrado con el mail " + mail));
	}

	@Override
	public void saveClient(Usuario usuario) {

		if (usuarioRepository.existsByMail(usuario.getMail())) {
			throw new ValidationException("Ya existe un usuario registrado con el mail " + usuario.getMail());
		}

		usuario.setRole(RolEnum.CLIENTE);

		usuarioRepository.save(usuario);

	}

	@Override
	public Usuario saveAdmin(Usuario usuario) {

		if (usuarioRepository.existsByMail(usuario.getMail())) {
			throw new ValidationException("Ya existe un usuario registrado con el mail " + usuario.getMail());
		}

		usuario.setRole(RolEnum.ADMIN);

		return usuarioRepository.save(usuario);

	}


}
