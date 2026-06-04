package servicio.service;

import java.util.List;

import servicio.entity.Servicio;
import servicio.repository.ServicioRepository;

public class ServicioServiceImpl implements ServicioService {
	private final ServicioRepository servicioRepository;

	public ServicioServiceImpl(ServicioRepository servicioRepository) {
		this.servicioRepository = servicioRepository;
	}

	@Override
	public List<Servicio> buscarPorNegocio(int idNegocio) {
		return servicioRepository.findByIdNegocio(idNegocio);
	}
}
