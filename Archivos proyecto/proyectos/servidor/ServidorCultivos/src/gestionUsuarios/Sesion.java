package gestionUsuarios;

public class Sesion {
	public enum SolicitudSesion {Nueva, Acceso, Quitar}
	private SolicitudSesion solicitud;
	private String usuario;
	private String uuid;
	public void crearSolicitudNueva(String usuario) {
		this.usuario = usuario;
		solicitud = SolicitudSesion.Nueva;
	}
	public void crearSolicitudAcceso(String usuario, String uuid) {
		this.usuario = usuario;
		this.uuid = uuid;
		solicitud = SolicitudSesion.Acceso;
	}
	public void crearSolicitudCerrarSesion(String uuid) {
		this.uuid = uuid;
		solicitud = SolicitudSesion.Quitar;
	}
	public SolicitudSesion getSolicitud() {
		return solicitud;
	}
	public String getUsuario() {
		return usuario;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
