package auth.service;

import java.sql.SQLException;

import auth.dto.LoginRequest;
import shared.exception.AuthenticationException;
import usuario.entity.Usuario;
import usuario.service.UsuarioService;

public class AuthService {
	private final UsuarioService usuarioService;

	public AuthService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public Usuario login(LoginRequest loginRequest) throws SQLException {

		Usuario usuario = usuarioService.findByMail(loginRequest.getMail());

		if (!usuario.getPass().equals(loginRequest.getPassword())) {
			throw new AuthenticationException("La contrasena ingresada es incorrecta");
		}

		return usuario;
	}
}
