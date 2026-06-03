package negocio.service;

import negocio.entity.Negocio;

import java.util.List;

public interface NegocioService {
    void createBusiness(Negocio negocio);
    List<Negocio> obtenerTodosLosNegocios();
}
