package negocio.repository;

import negocio.entity.Negocio;

import java.util.List;

public interface NegocioRepository {
    void save(Negocio negocio);
    List<Negocio> findAll();
}
