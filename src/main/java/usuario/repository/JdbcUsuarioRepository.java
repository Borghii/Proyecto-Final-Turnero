package usuario.repository;

import java.sql.*;
import java.util.Optional;

import shared.db.ConexionDB;
import shared.enums.RolEnum;
import shared.exception.InternalSystemException;
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
	public Optional<Usuario> findByMail(String mail) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_MAIL)) {

			statement.setString(1, mail);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(mapUsuario(resultSet));
				}

				return Optional.empty();
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public boolean existsByMail(String mail) {
		try (Connection connection = conexionDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(EXISTS_BY_MAIL)) {

			statement.setString(1, mail);

			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}

	@Override
	public Usuario save(Usuario usuario) {
		try (Connection connection = conexionDB.getConnection();
		     PreparedStatement statement = connection.prepareStatement(
					 INSERT_CLIENT,
					 Statement.RETURN_GENERATED_KEYS)) {

			statement.setString(1, usuario.getMail());
			statement.setString(2, usuario.getPass());
			statement.setString(3, usuario.getNombre());
			statement.setInt(4, usuario.getRole().getIdRol());


			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return new Usuario(
							generatedKeys.getInt(1),
							usuario.getMail(),
							usuario.getPass(),
							usuario.getNombre(),
							usuario.getRole());
				}

				throw new InternalSystemException();
			}
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
    }


	private Usuario mapUsuario(ResultSet resultSet) {
		try {
			return new Usuario(
					resultSet.getInt("id_usuario"),
					resultSet.getString("mail"),
					resultSet.getString("pass"),
					resultSet.getString("nombre"),
					RolEnum.valueOf(resultSet.getString("role_name")));
		} catch (SQLException e) {
			throw new InternalSystemException();
		}
	}
}
