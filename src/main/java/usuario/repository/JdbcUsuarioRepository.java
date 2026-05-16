package usuario.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import shared.db.ConexionDB;
import shared.enums.RolEnum;
import usuario.entity.Usuario;

public class JdbcUsuarioRepository implements UsuarioRepository {

	private static final String SELECT_BY_MAIL = """
			SELECT u.id_usuario, u.mail, u.pass, u.nombre, r.nombre AS role_name
			FROM usuarios u
			JOIN roles r ON r.id_rol = u.id_rol
			WHERE u.mail = ?
			""";

	private static final String EXISTS_BY_MAIL = """
			SELECT 1
			FROM usuarios
			WHERE mail = ?
			LIMIT 1
			""";

	private static final String INSERT_CLIENT = """
			INSERT INTO usuarios (mail, pass, nombre, id_rol)
			VALUES (?, ?, ?, ?)
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

	@Override
	public boolean existsByMail(String mail) throws SQLException {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(EXISTS_BY_MAIL)) {

			statement.setString(1, mail);

			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
		}
	}

	@Override
	public void save(Usuario usuario) throws SQLException {
		try (Connection connection = conexionDB.getConnection();
		     PreparedStatement statement = connection.prepareStatement(INSERT_CLIENT)) {

			statement.setString(1, usuario.getMail());
			statement.setString(2, usuario.getPass());
			statement.setString(3, usuario.getNombre());
			statement.setInt(4, usuario.getRole().getIdRol());


			statement.executeUpdate();
		}
    }


	private Usuario mapUsuario(ResultSet resultSet) throws SQLException {
		return new Usuario(
				resultSet.getInt("id_usuario"),
				resultSet.getString("mail"),
				resultSet.getString("pass"),
				resultSet.getString("nombre"),
				RolEnum.valueOf(resultSet.getString("role_name")));
	}
}
