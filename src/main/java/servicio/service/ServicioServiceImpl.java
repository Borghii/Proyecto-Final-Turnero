package servicio.service;

import java.util.List;

import negocio.repository.NegocioRepository;
import servicio.entity.Servicio;
import servicio.repository.ServicioRepository;
import shared.exception.AuthorizationException;
import shared.exception.BusinessRuleException;
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

	@Override
	public void guardarServicio(Servicio servicio) {
		validarMultiploDe30(servicio.getDuracion());
		servicioRepository.save(servicio);
	}

	@Override
	public void actualizarServicio(Servicio servicio) {
		validarMultiploDe30(servicio.getDuracion());

		Servicio existente = servicioRepository.findById(servicio.getIdServicio())
				.orElseThrow(() -> new ResourceNotFoundException("El servicio no existe"));

		if (existente.getIdNegocio() != servicio.getIdNegocio()) {
			throw new AuthorizationException("No tenes permiso para editar este servicio.");
		}

		servicioRepository.update(servicio);
	}

	@Override
	public void eliminarServicio(int idServicio, int idNegocio) {
		Servicio existente = servicioRepository.findById(idServicio)
				.orElseThrow(() -> new ResourceNotFoundException("El servicio no existe"));

		if (existente.getIdNegocio() != idNegocio) {
			throw new AuthorizationException("No tenes permiso para eliminar este servicio.");
		}

		servicioRepository.delete(idServicio);
	}

	private void validarMultiploDe30(int duracion) {
		if (duracion <= 0 || duracion % 30 != 0) {
			throw new BusinessRuleException(
					"La duracion del servicio debe ser un multiplo exacto de 30 minutos (ej: 30, 60, 90).");
		}
	}
}
