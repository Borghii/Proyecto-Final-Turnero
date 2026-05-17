package usuario.service;

import java.sql.SQLException;

import auth.dto.SignUpClientRequest;
import usuario.entity.Usuario;

public interface UsuarioService {
	Usuario findByMail(String mail) throws SQLException;

    void saveClient(Usuario usuario) throws SQLException;

    Usuario saveAdmin(Usuario usuario) throws SQLException;
}
