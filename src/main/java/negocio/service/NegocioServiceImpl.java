package negocio.service;

import negocio.dto.NegocioConHorarios;
import negocio.entity.Horario;
import negocio.entity.Negocio;
import negocio.repository.HorarioRepository;
import negocio.repository.NegocioRepository;
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
        // Le pedimos al repositorio la lista que armaste en el paso anterior
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
}
