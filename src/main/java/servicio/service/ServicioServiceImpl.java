package servicio.service;

import servicio.repository.ServicioRepository;

public class ServicioServiceImpl implements ServicioService {
	private final ServicioRepository servicioRepository;

	public ServicioServiceImpl(ServicioRepository servicioRepository) {
		this.servicioRepository = servicioRepository;
	}
}
