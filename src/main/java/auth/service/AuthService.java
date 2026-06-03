package auth.service;

import auth.dto.LoginRequest;
import auth.dto.SignUpBussinesRequest;
import auth.dto.SignUpClientRequest;
import usuario.entity.Usuario;

public interface AuthService {
	Usuario login(LoginRequest loginRequest);

	void signUpClient(SignUpClientRequest signUpClientRequest);

	void signUpBusiness(SignUpBussinesRequest signUpBussinesRequest);
}
