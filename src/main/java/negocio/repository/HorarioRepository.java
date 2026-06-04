package negocio.repository;

import negocio.entity.Horario;

import java.util.List;

public interface HorarioRepository {
	void saveAll(int idNegocio, List<Horario> horarios);
	List<Horario> findByNegocioId(int idNegocio);
}
