package sistemaExperto;


public class SistemaExpertoInsectos {
	public static final String PROBLEMA = "insectos";
	private String agujerosHojas;
	private String agujerosTallo;
	private String agujerosFrutos;
	private String insectosCerca;
	private int porcentaje;
	public SistemaExpertoInsectos() {
		porcentaje = 0;
	}
	public void aplicarSistemaExperto() {
		porcentaje = 0;
		if(agujerosHojas.equals("OK")) {
			porcentaje += 50;
		}
		if(agujerosTallo.equals("OK")) {
			porcentaje += 25;
		}
		if(agujerosFrutos.equals("OK")) {
			porcentaje += 50;
		}
		if(insectosCerca.equals("OK")) {
			porcentaje += 50;
		}
	}
	public boolean getResultado() {
		return porcentaje > 50;
	}
	public String getAgujerosHojas() {
		return agujerosHojas;
	}
	public void setAgujerosHojas(String agujerosHojas) {
		if(agujerosHojas == null) {
			this.agujerosHojas = "NOK";
		}
		else {
			this.agujerosHojas = agujerosHojas;
		}
	}
	public String getAgujerosTallo() {
		return agujerosTallo;
	}
	public void setAgujerosTallo(String agujerosTallo) {
		if(agujerosTallo == null) {
			this.agujerosTallo = "NOK";
		}
		else {
			this.agujerosTallo = agujerosTallo;
		}
		
	}
	public String getAgujerosFrutos() {
		return agujerosFrutos;
	}
	public void setAgujerosFrutos(String agujerosFrutos) {
		if(agujerosFrutos == null) {
			this.agujerosFrutos = "NOK";
		}
		else {
			this.agujerosFrutos = agujerosFrutos;
		}
	}
	public String getInsectosCerca() {
		return insectosCerca;
	}
	public void setInsectosCerca(String insectosCerca) {
		if(insectosCerca == null) {
			this.insectosCerca = "NOK";
		}
		else {
			this.insectosCerca = insectosCerca;
		}
		
	}
	
}
