package negocio.service;

import negocio.dto.ActualizarNegocioRequest;
import negocio.dto.NegocioConHorarios;
import negocio.entity.Negocio;

import java.util.List;

public interface NegocioService {
	void createBusiness(Negocio negocio);

	List<Negocio> obtenerTodosLosNegocios();

	NegocioConHorarios obtenerNegocioConHorarios(int userId);

	void actualizarNegocio(int userId, ActualizarNegocioRequest request);

	Negocio obtenerNegocioPorUsuario(int idUsuario);
}
