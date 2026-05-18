package turno.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import shared.enums.EstadoTurnoEnum;

public class TurnoClienteDTO {
    private int idTurno;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String nombreNegocio;
    private String nombreServicio;
    private double precio;
    private EstadoTurnoEnum estado;

    public TurnoClienteDTO(int idTurno, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, 
                           String nombreNegocio, String nombreServicio, double precio, EstadoTurnoEnum estado) {
        this.idTurno = idTurno;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.nombreNegocio = nombreNegocio;
        this.nombreServicio = nombreServicio;
        this.precio = precio;
        this.estado = estado;
    }

    public int getIdTurno() { return idTurno; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public String getNombreNegocio() { return nombreNegocio; }
    public String getNombreServicio() { return nombreServicio; }
    public double getPrecio() { return precio; }
    public EstadoTurnoEnum getEstado() { return estado; }
}