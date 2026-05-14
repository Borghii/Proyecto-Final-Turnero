package usuario.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import shared.db.ConexionDB;
import usuario.entity.Usuario;

public class JdbcUsuarioRepository implements UsuarioRepository {

	private static final String SELECT_BY_MAIL = """
			SELECT id_usuario, mail, pass, nombre, id_rol
			FROM usuarios
			WHERE mail = ?
			""";


	private final ConexionDB conexionDB;

	public JdbcUsuarioRepository(ConexionDB conexionDB) {
		this.conexionDB = conexionDB;
	}

	@Override
	public Optional<Usuario> findByMail(String mail) throws SQLException {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_MAIL)) {

			statement.setString(1, mail);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(mapUsuario(resultSet));
				}

				return Optional.empty();
			}
		}
	}


	private Usuario mapUsuario(ResultSet resultSet) throws SQLException {
		return new Usuario(
				resultSet.getInt("id_usuario"),
				resultSet.getString("mail"),
				resultSet.getString("pass"),
				resultSet.getString("nombre"),
				resultSet.getInt("id_rol"));
	}
}
