package servicio.repository;

import java.util.List;
import java.util.Optional;

import servicio.entity.Servicio;

public interface ServicioRepository {
	List<Servicio> findByIdNegocio(int idNegocio);

	Optional<Servicio> findById(int idServicio);

	boolean existsActivoByNombreAndNegocio(String nombre, int idNegocio);

	boolean existsActivoByNombreAndNegocioExcludingId(String nombre, int idNegocio, int idServicio);

	void save(Servicio servicio);

	void update(Servicio servicio);

	void delete(int idServicio, int idNegocio);
}
