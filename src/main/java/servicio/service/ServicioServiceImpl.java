package servicio.service;

import java.util.List;

import negocio.repository.NegocioRepository;
import servicio.entity.Servicio;
import servicio.repository.ServicioRepository;
import shared.exception.ResourceNotFoundException;

public class ServicioServiceImpl implements ServicioService {
	private final ServicioRepository servicioRepository;
	private final NegocioRepository negocioRepository;

	public ServicioServiceImpl(ServicioRepository servicioRepository, NegocioRepository negocioRepository) {
		this.servicioRepository = servicioRepository;
		this.negocioRepository = negocioRepository;
	}

	@Override
	public List<Servicio> buscarPorNegocio(int idNegocio) {
		negocioRepository.findById(idNegocio)
				.orElseThrow(() -> new ResourceNotFoundException("El negocio indicado no existe"));

		return servicioRepository.findByIdNegocio(idNegocio);
	}
}
