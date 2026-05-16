package auth.service;

import java.sql.SQLException;

import auth.dto.LoginRequest;
import auth.dto.SignUpClientRequest;
import usuario.entity.Usuario;

public interface AuthService {
	Usuario login(LoginRequest loginRequest) throws SQLException;

	void signUpClient(SignUpClientRequest signUpClientRequest) throws SQLException;
}
