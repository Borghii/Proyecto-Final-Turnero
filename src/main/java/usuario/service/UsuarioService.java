package usuario.service;

import usuario.entity.Usuario;

public interface UsuarioService {
	Usuario findByMail(String mail);

    void saveClient(Usuario usuario);

    Usuario saveAdmin(Usuario usuario);
}
