package servicio.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import negocio.entity.Negocio;
import negocio.repository.NegocioRepository;
import servicio.dto.ActualizarServicioRequest;
import servicio.dto.EliminarServicioRequest;
import servicio.dto.GuardarServicioRequest;
import servicio.entity.Servicio;
import servicio.repository.ServicioRepository;
import shared.exception.AuthorizationException;
import shared.exception.BusinessRuleException;
import shared.exception.ResourceNotFoundException;
import turno.repository.TurnoRepository;

public class ServicioServiceImpl implements ServicioService {
	private final ServicioRepository servicioRepository;
	private final NegocioRepository negocioRepository;
	private final TurnoRepository turnoRepository;

	public ServicioServiceImpl(ServicioRepository servicioRepository, NegocioRepository negocioRepository,
			TurnoRepository turnoRepository) {
		this.servicioRepository = servicioRepository;
		this.negocioRepository = negocioRepository;
		this.turnoRepository = turnoRepository;
	}

	@Override
	public List<Servicio> buscarPorNegocio(int idNegocio) {
		negocioRepository.findById(idNegocio)
				.orElseThrow(() -> new ResourceNotFoundException("El negocio indicado no existe"));

		return servicioRepository.findByIdNegocio(idNegocio);
	}

	@Override
	public List<Servicio> buscarPorDuenioDeNegocio(int idUsuario) {
		Negocio negocio = obtenerNegocioPorUsuario(idUsuario);
		return servicioRepository.findByIdNegocio(negocio.getIdNegocio());
	}

	@Override
	public void guardarServicio(int idUsuario, GuardarServicioRequest request) {
		Negocio negocio = obtenerNegocioPorUsuario(idUsuario);
		validarMultiploDe30(request.getDuracion());
		validarNombreDisponible(request.getNombre(), negocio.getIdNegocio());

		Servicio servicio = new Servicio(
				request.getNombre(),
				request.getPrecio(),
				request.getDuracion(),
				negocio.getIdNegocio());
		servicioRepository.save(servicio);
	}

	@Override
	public void actualizarServicio(int idUsuario, ActualizarServicioRequest request) {
		Negocio negocio = obtenerNegocioPorUsuario(idUsuario);
		validarMultiploDe30(request.getDuracion());

		Servicio existente = servicioRepository.findById(request.getIdServicio())
				.orElseThrow(() -> new ResourceNotFoundException("El servicio no existe"));

		if (existente.getIdNegocio() != negocio.getIdNegocio()) {
			throw new AuthorizationException("No tenes permiso para editar este servicio.");
		}

		validarNombreDisponibleParaEditar(request.getNombre(), negocio.getIdNegocio(), request.getIdServicio());

		if (turnoRepository.existsActivosByServicioFromDateTime(
				request.getIdServicio(),
				LocalDate.now(),
				LocalTime.now())) {
			throw new BusinessRuleException(
					"No podes modificar un servicio que tiene turnos proximos confirmados. Cancela esos turnos antes de modificarlo.");
		}

		Servicio servicio = new Servicio(
				request.getIdServicio(),
				request.getNombre(),
				request.getPrecio(),
				request.getDuracion(),
				negocio.getIdNegocio());

		servicioRepository.update(servicio);
	}

	@Override
	public void eliminarServicio(int idUsuario, EliminarServicioRequest request) {
		Negocio negocio = obtenerNegocioPorUsuario(idUsuario);

		Servicio existente = servicioRepository.findById(request.getIdServicio())
				.orElseThrow(() -> new ResourceNotFoundException("El servicio no existe"));

		if (existente.getIdNegocio() != negocio.getIdNegocio()) {
			throw new AuthorizationException("No tenes permiso para eliminar este servicio.");
		}

		if (turnoRepository.existsActivosByServicioFromDateTime(
				request.getIdServicio(),
				LocalDate.now(),
				LocalTime.now())) {
			throw new BusinessRuleException(
					"No podes eliminar un servicio que tiene turnos proximos confirmados. Cancela esos turnos antes de eliminarlo.");
		}

		servicioRepository.delete(request.getIdServicio(), negocio.getIdNegocio());
	}

	private void validarMultiploDe30(int duracion) {
		if (duracion <= 0 || duracion % 30 != 0) {
			throw new BusinessRuleException(
					"La duracion del servicio debe ser un multiplo exacto de 30 minutos (ej: 30, 60, 90).");
		}
	}

	private void validarNombreDisponible(String nombre, int idNegocio) {
		if (servicioRepository.existsActivoByNombreAndNegocio(nombre, idNegocio)) {
			throw new BusinessRuleException("Ya existe un servicio con ese nombre.");
		}
	}

	private void validarNombreDisponibleParaEditar(String nombre, int idNegocio, int idServicio) {
		if (servicioRepository.existsActivoByNombreAndNegocioExcludingId(nombre, idNegocio, idServicio)) {
			throw new BusinessRuleException("Ya existe otro servicio con ese nombre.");
		}
	}

	private Negocio obtenerNegocioPorUsuario(int idUsuario) {
		return negocioRepository.findByUserId(idUsuario)
				.orElseThrow(() -> new ResourceNotFoundException("Este usuario no tiene un negocio registrado."));
	}
}
