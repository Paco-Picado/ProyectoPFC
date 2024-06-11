package sistemaExperto;


public class SistemaExpertoHongos {
	public static final String PROBLEMA = "hongos";
	private String moho;
	private String manchas;
	private String humedad;
	private String podrido;
	private int porcentaje;
	public SistemaExpertoHongos() {
		porcentaje = 0;
	}
	public void aplicarSistemaExperto() {
		porcentaje = 0;
		if(moho.equals("OK")) {
			porcentaje += 100;
		}
		if(manchas.equals("OK")) {
			porcentaje += 25;
		}
		if(humedad.equals("OK")) {
			porcentaje += 25;
		}
		if(podrido.equals("OK")) {
			porcentaje += 50;
		}
	}
	public boolean getResultado() {
		return porcentaje > 50;
	}

	public String getMoho() {
		return moho;
	}
	public void setMoho(String moho) {
		if(moho == null) {
			this.moho = "NOK";
		}
		else {
			this.moho = moho;
		}
	}
	public String getManchas() {
		return manchas;
	}
	public void setManchas(String manchas) {
		if(manchas == null) {
			this.manchas = "NOK";
		}
		else {
			this.manchas = manchas;
		}		
	}
	public String getHumedad() {
		return humedad;
	}
	public void setHumedad(String humedad) {
		if(humedad == null) {
			this.humedad = "NOK";
		}
		else {
			this.humedad = humedad;
		}		
	}
	public String getPodrido() {
		return podrido;
	}
	public void setPodrido(String podrido) {
		if(podrido == null) {
			this.podrido = "NOK";
		}
		else {
			this.podrido = podrido;
		}
	}
	
}
