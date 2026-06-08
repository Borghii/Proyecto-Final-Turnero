package servicio.service;

import java.util.List;

import servicio.dto.ActualizarServicioRequest;
import servicio.dto.EliminarServicioRequest;
import servicio.dto.GuardarServicioRequest;
import servicio.entity.Servicio;

public interface ServicioService {
	List<Servicio> buscarPorNegocio(int idNegocio);

	List<Servicio> buscarPorDuenioDeNegocio(int idUsuario);

	void guardarServicio(int idUsuario, GuardarServicioRequest request);

	void actualizarServicio(int idUsuario, ActualizarServicioRequest request);

	void eliminarServicio(int idUsuario, EliminarServicioRequest request);
}
