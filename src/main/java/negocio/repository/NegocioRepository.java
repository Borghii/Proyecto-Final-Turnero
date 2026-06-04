package negocio.repository;

import negocio.entity.Negocio;

import java.util.List;
import java.util.Optional;

public interface NegocioRepository {
    Negocio save(Negocio negocio);
    List<Negocio> findAll();
    Optional<Negocio> findByUserId(int userId);
}
