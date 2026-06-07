package negocio.service;

import negocio.dto.ActualizarHorarioRequest;
import negocio.dto.ActualizarNegocioRequest;
import negocio.dto.NegocioConHorarios;
import negocio.entity.Horario;
import negocio.entity.Negocio;
import negocio.repository.HorarioRepository;
import negocio.repository.NegocioRepository;
import shared.exception.BusinessRuleException;
import shared.exception.ResourceNotFoundException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class NegocioServiceImpl implements NegocioService {
	private final NegocioRepository negocioRepository;
	private final HorarioRepository horarioRepository;

	public NegocioServiceImpl(NegocioRepository negocioRepository, HorarioRepository horarioRepository) {
		this.negocioRepository = negocioRepository;
		this.horarioRepository = horarioRepository;
	}

	@Override
	public void createBusiness(Negocio negocio) {
		Negocio negocioGuardado = negocioRepository.save(negocio);
		horarioRepository.saveAll(negocioGuardado.getIdNegocio(), horariosDefault());
	}

	@Override
	public List<Negocio> obtenerTodosLosNegocios() {
		return negocioRepository.findAll();
	}

	@Override
	public NegocioConHorarios obtenerNegocioConHorarios(int userId) {
		Negocio negocio = negocioRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontro el negocio"));

		NegocioConHorarios negocioConHorarios = new NegocioConHorarios();
		negocioConHorarios.setNombre(negocio.getNombre());
		negocioConHorarios.setCiudad(negocio.getCiudad());
		negocioConHorarios.setHorarios(horarioRepository.findByNegocioId(negocio.getIdNegocio()));

		return negocioConHorarios;
	}

	@Override
	public void actualizarNegocio(int userId, ActualizarNegocioRequest request) {
		Negocio negocio = negocioRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontro el negocio"));

		List<Horario> horarios = new ArrayList<>();
		for (ActualizarHorarioRequest horarioRequest : request.getHorarios()) {
			horarios.add(crearHorario(horarioRequest));
		}

		negocioRepository.update(new Negocio(
				negocio.getIdNegocio(),
				request.getCiudad(),
				request.getNombre(),
				userId));

		horarioRepository.updateAll(negocio.getIdNegocio(), horarios);
	}

	private Horario crearHorario(ActualizarHorarioRequest horarioRequest) {
		if (!horarioRequest.isActivo()) {
			return new Horario(horarioRequest.getDia(), null, null, null, null, false);
		}

		validarBloqueCompleto(horarioRequest.getMananaDesde(), horarioRequest.getMananaHasta(), "manana");
		validarBloqueCompleto(horarioRequest.getTardeDesde(), horarioRequest.getTardeHasta(), "tarde");

		boolean tieneManana = horarioRequest.getMananaDesde() != null;
		boolean tieneTarde = horarioRequest.getTardeDesde() != null;

		if (!tieneManana && !tieneTarde) {
			throw new BusinessRuleException("Un dia activo debe tener al menos una franja horaria");
		}

		if (tieneManana && !horarioRequest.getMananaHasta().isAfter(horarioRequest.getMananaDesde())) {
			throw new BusinessRuleException("La hora de fin de la manana debe ser posterior a la hora de inicio");
		}

		if (tieneTarde && !horarioRequest.getTardeHasta().isAfter(horarioRequest.getTardeDesde())) {
			throw new BusinessRuleException("La hora de fin de la tarde debe ser posterior a la hora de inicio");
		}

		if (tieneManana && tieneTarde && horarioRequest.getTardeDesde().isBefore(horarioRequest.getMananaHasta())) {
			throw new BusinessRuleException("La tarde no puede empezar antes de que termine la manana");
		}

		return new Horario(
				horarioRequest.getDia(),
				horarioRequest.getMananaDesde(),
				horarioRequest.getMananaHasta(),
				horarioRequest.getTardeDesde(),
				horarioRequest.getTardeHasta(),
				true);
	}

	private void validarBloqueCompleto(LocalTime desde, LocalTime hasta, String bloque) {
		if ((desde == null && hasta != null) || (desde != null && hasta == null)) {
			throw new BusinessRuleException("La franja de " + bloque + " debe tener hora desde y hora hasta");
		}
	}

	private List<Horario> horariosDefault() {
		List<Horario> horarios = new ArrayList<>();

		for (int dia = 1; dia <= 7; dia++) {
			boolean activo = dia <= 5;

			horarios.add(new Horario(
					dia,
					activo ? LocalTime.of(9, 0) : null,
					activo ? LocalTime.of(12, 0) : null,
					activo ? LocalTime.of(16, 0) : null,
					activo ? LocalTime.of(20, 0) : null,
					activo));
		}

		return horarios;
	}
	
	@Override
    public Negocio obtenerNegocioPorUsuario(int idUsuario) {
        return negocioRepository.findByUserId(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Este usuario no tiene un negocio registrado."));
    }
}
