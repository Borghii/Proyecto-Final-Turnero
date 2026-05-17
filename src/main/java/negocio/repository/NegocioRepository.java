package negocio.repository;

import negocio.entity.Negocio;

import java.sql.SQLException;

public interface NegocioRepository {
    void save(Negocio negocio) throws SQLException;
}
