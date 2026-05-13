package auth.service;

import auth.dto.LoginRequest;
import usuario.repository.UsuarioRepository;

public class AuthService {
	private final UsuarioRepository usuarioRepository;

	public AuthService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public void login(LoginRequest loginRequest) {
		// TODO: validar credenciales contra UsuarioRepository.
	}
}
