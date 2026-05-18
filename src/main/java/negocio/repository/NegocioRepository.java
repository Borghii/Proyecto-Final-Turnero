package negocio.repository;

import negocio.entity.Negocio;

import java.sql.SQLException;
import java.util.List;

public interface NegocioRepository {
    void save(Negocio negocio) throws SQLException;
    List<Negocio> findAll() throws SQLException;
}
