package negocio.service;

import negocio.repository.NegocioRepository;

public class NegocioServiceImpl implements NegocioService {
	private final NegocioRepository negocioRepository;

	public NegocioServiceImpl(NegocioRepository negocioRepository) {
		this.negocioRepository = negocioRepository;
	}
}
