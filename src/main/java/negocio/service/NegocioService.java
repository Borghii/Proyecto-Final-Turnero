package negocio.service;

import negocio.repository.NegocioRepository;

public class NegocioService {
	private final NegocioRepository negocioRepository;

	public NegocioService(NegocioRepository negocioRepository) {
		this.negocioRepository = negocioRepository;
	}
}
