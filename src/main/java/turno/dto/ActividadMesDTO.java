package turno.dto;

public class ActividadMesDTO {
	private int totalTurnos;
	private int turnosRealizados;
	private int turnosCancelados;
	private double ingresosMes;

	public ActividadMesDTO(int totalTurnos, int turnosRealizados, int turnosCancelados, double ingresosMes) {
		this.totalTurnos = totalTurnos;
		this.turnosRealizados = turnosRealizados;
		this.turnosCancelados = turnosCancelados;
		this.ingresosMes = ingresosMes;
	}

	public int getTotalTurnos() { return totalTurnos; }
	public int getTurnosRealizados() { return turnosRealizados; }
	public int getTurnosCancelados() { return turnosCancelados; }
	public double getIngresosMes() { return ingresosMes; }
}