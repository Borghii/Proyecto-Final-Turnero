package auth.service;

import java.sql.SQLException;

import auth.dto.LoginRequest;
import auth.dto.SignUpClientRequest;
import shared.exception.AuthenticationException;
import usuario.entity.Usuario;
import usuario.service.UsuarioService;

public class AuthServiceImpl implements AuthService {
	private final UsuarioService usuarioService;

	public AuthServiceImpl(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Override
	public Usuario login(LoginRequest loginRequest) throws SQLException {

		Usuario usuario = usuarioService.findByMail(loginRequest.getMail());

		if (!usuario.getPass().equals(loginRequest.getPassword())) {
			throw new AuthenticationException("La contrasena ingresada es incorrecta");
		}

		return usuario;
	}

	@Override
	public void signUpClient(SignUpClientRequest signUpClientRequest) throws SQLException {


		usuarioService.saveClient(new Usuario(
				signUpClientRequest.getMail(),
				signUpClientRequest.getPassword(),
				signUpClientRequest.getNombre()));



	}
}
