package tipoPeticiones;

public class Peticion {
	public static final String LOGIN = "login";
	public static final String REGISTRO = "registro";
	public static final String CULTIVO = "cultivo";
	public static final String PROBLEMA = "problema";
	public static final String CERRAR_SESION = "cerrar";
	private String tipo;
	private String usuario;
	private String password;
	private String sesion;
	private String cultivo;
	private String fase;	
	private PeticionProblema problema;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
	public String getCultivo() {
		return cultivo;
	}
	public void setCultivo(String cultivo) {
		this.cultivo = cultivo;
	}
	public String getFase() {
		return fase;
	}
	public void setFase(String fase) {
		this.fase = fase;
	}
	public PeticionProblema getProblema() {
		return problema;
	}
	public void setProblema(PeticionProblema problema) {
		this.problema = problema;
	}
	public String getSesion() {
		return sesion;
	}
	public void setSesion(String sesion) {
		this.sesion = sesion;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
