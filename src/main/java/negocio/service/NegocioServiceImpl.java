package negocio.service;

import negocio.entity.Negocio;
import negocio.repository.NegocioRepository;

import java.util.List;

public class NegocioServiceImpl implements NegocioService {
	private final NegocioRepository negocioRepository;

	public NegocioServiceImpl(NegocioRepository negocioRepository) {
		this.negocioRepository = negocioRepository;
	}

	@Override
	public void createBusiness(Negocio negocio) {
		negocioRepository.save(negocio);
	}
	@Override
    public List<Negocio> obtenerTodosLosNegocios() {
        // Le pedimos al repositorio la lista que armaste en el paso anterior
        return negocioRepository.findAll(); 
    }
}
