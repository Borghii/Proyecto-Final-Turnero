package servicio.service;

import java.util.List;

import servicio.entity.Servicio;

public interface ServicioService {
	List<Servicio> buscarPorNegocio(int idNegocio);
	
	void guardarServicio(Servicio servicio);
	
	void actualizarServicio(Servicio servicio);
	
	void eliminarServicio(int idServicio, int idNegocio);
}