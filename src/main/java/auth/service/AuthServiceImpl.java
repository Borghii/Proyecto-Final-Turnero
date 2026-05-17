package auth.service;

import java.sql.SQLException;

import auth.dto.LoginRequest;
import auth.dto.SignUpBussinesRequest;
import auth.dto.SignUpClientRequest;
import negocio.entity.Negocio;
import negocio.service.NegocioService;
import shared.exception.AuthenticationException;
import usuario.entity.Usuario;
import usuario.service.UsuarioService;

public class AuthServiceImpl implements AuthService {

	private final UsuarioService usuarioService;
	private final NegocioService negocioService;

	public AuthServiceImpl(UsuarioService usuarioService, NegocioService negocioService) {
		this.usuarioService = usuarioService;
        this.negocioService = negocioService;
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

	@Override
	public void signUpBusiness(SignUpBussinesRequest signUpBussinesRequest) throws SQLException {


		//Nota: estas dos operaciones se deberin realizar dentro de una transaccion
		//por simplicidad lo vamos a realizar en transacciones diferentes

		Usuario usuario = usuarioService.saveAdmin(new Usuario(
				signUpBussinesRequest.getSignUpClientRequest().getMail(),
				signUpBussinesRequest.getSignUpClientRequest().getPassword(),
				signUpBussinesRequest.getSignUpClientRequest().getNombre()));

		negocioService.createBusiness(new Negocio(
				signUpBussinesRequest.getCiudad(),
				signUpBussinesRequest.getNombre(),
				usuario.getIdUsuario()
		));



	}
}
