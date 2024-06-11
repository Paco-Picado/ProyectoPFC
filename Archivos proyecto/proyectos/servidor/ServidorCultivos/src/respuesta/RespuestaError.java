package respuesta;

public class RespuestaError {
	public static final String[] PROTOCOLO_ERROR = {"PROTOCOL", "El protocolo no sigue el formato json"};
	public static final String[] PETICION_DESCONOCIDA = {"PETICION", "PETICION DESCONOCIDA"};
	public static final String[] REG_USUARIO_EXISTE = {"USER","El usuario ya existe"};
	public static final String[] REG_PASSWD_NO_VALIDA = {"PASSWD", "La contraseña no cumple los criterios"};
	public static final String[] FALLO_DESCONOCIDO  = {"ERROR","Vuelva a intentarlo"};
	public static final String[] LOG_PASSWD_USER  = {"PASSWD-USER","Usuario o password incorrectos"};
	public static final String[] SESION_ERROR = {"SESION", "Sesion caducada"};
	private String causa;
	private String mensaje;
	public RespuestaError(String[] error) {
		this.causa = error[0];
		this.mensaje = error[1];
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getCausa() {
		return causa;
	}
	public void setCausa(String causa) {
		this.causa = causa;
	}
	
}
