package gestionUsuarios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Operacion {
	public enum TipoDeOperacion {Registro, Login}	
	private static final String PATRON = "^[a-zA-Z0-9!@#$%^&*()_+={};':,.<>?]*$";
//Esta clase se encarga de crear operaciones de acceso de la base de datos de usuarios
	private String usuario;
	private String password;
	private TipoDeOperacion tipo;
	public String getUsuario() {
		return usuario;
	}
	public String getPassword() {
		return password;
	}
	public TipoDeOperacion getTipo() {
		return tipo;
	}
	public void setTipo(TipoDeOperacion tipo) {
		this.tipo = tipo;
	}
	public void crearOperacionRegistro(String usuario, String password) {
		this.usuario = usuario;
		this.password = convertirEnHashCodificado(password);
		this.tipo = TipoDeOperacion.Registro;
	}
	public void crearOperacionLogin(String usuario, String password) {
		this.usuario = usuario;
		this.password = convertirEnHashCodificado(password);
		this.tipo = TipoDeOperacion.Login;
	}
	private String convertirEnHashCodificado(String password) {
		String hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			hash =  Base64.getEncoder().encodeToString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return hash;
	}
}
