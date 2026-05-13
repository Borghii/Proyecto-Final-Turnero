package servicio.service;

import servicio.repository.ServicioRepository;

public class ServicioService {
	private final ServicioRepository servicioRepository;

	public ServicioService(ServicioRepository servicioRepository) {
		this.servicioRepository = servicioRepository;
	}
}
