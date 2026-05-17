package negocio.service;

import negocio.entity.Negocio;

import java.sql.SQLException;

public interface NegocioService {
    void createBusiness(Negocio negocio) throws SQLException;
}
