package negocio.dto;

import negocio.entity.Horario;

import java.util.ArrayList;
import java.util.List;

public class NegocioConHorarios {

    private String ciudad;
    private String nombre;

    private List<Horario> horarios = new ArrayList<>();

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }
}
