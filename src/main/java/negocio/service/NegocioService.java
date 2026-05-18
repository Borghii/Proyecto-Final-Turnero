package negocio.service;

import negocio.entity.Negocio;

import java.sql.SQLException;
import java.util.List;

public interface NegocioService {
    void createBusiness(Negocio negocio) throws SQLException;
    List<Negocio> obtenerTodosLosNegocios() throws SQLException;
}
