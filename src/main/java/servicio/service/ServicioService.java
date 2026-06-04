package servicio.service;

import java.util.List;

import servicio.entity.Servicio;

public interface ServicioService {
	List<Servicio> buscarPorNegocio(int idNegocio);
}
