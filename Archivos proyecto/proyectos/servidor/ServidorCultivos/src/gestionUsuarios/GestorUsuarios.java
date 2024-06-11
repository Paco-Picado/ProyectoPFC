package gestionUsuarios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gestionUsuarios.Operacion.TipoDeOperacion;
import respuesta.RespuestaError;
import tipoPeticiones.PeticionFallidaException;

public class GestorUsuarios {
	private Connection conn;
	private static final String INSERTAR_USUARIO = "insert into usuarios(usuario,passwd) values (?,?)";
	private static final String COMPROBAR_USUARIO= "select * from usuarios where usuario = ?";
	private static final String COMPROBAR_LOGIN = "select * from usuarios where usuario = ? and passwd = ?";
	private void abrirConexion() throws PeticionFallidaException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_usuarios","root","123qwe");
		} catch (Exception e) {
			throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
		}	
	}
	//Esta clase hace una operacion de registro o login, si existe una fallo lanzará excepción
	public synchronized void hacerOperacionConUsuario(Operacion o) throws PeticionFallidaException {
		abrirConexion();
		if(o.getTipo() == TipoDeOperacion.Registro) {
			//Registro
			hacerOperacionRegistro(o);
		}
		else if(o.getTipo() == TipoDeOperacion.Login) {
			//Login
			hacerOperacionLogin(o);
		}		
		cerrarConexion();
	}
	private void cerrarConexion() throws PeticionFallidaException {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
		}
		
	}
	//Realiza operación de Login
	private void hacerOperacionLogin(Operacion o) throws PeticionFallidaException {
		if(login(o)) {
			//LOGIN CORRECTO			
		}
		else {
			throw new PeticionFallidaException(RespuestaError.LOG_PASSWD_USER);
		}
	}
	//Realiza operación de registro
	private void hacerOperacionRegistro(Operacion o) throws PeticionFallidaException {
		if(existeUsuario(o.getUsuario())) {
			throw new PeticionFallidaException(RespuestaError.REG_USUARIO_EXISTE);
		}
		else {
			if(registrar(o)) {
				//Registro correcto
			}
			else {
				throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
			}
		}
	}
	//Está función comprueba que existe un usuario previamente al registro
	private boolean existeUsuario(String usuario) {
		boolean existe = true;
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(GestorUsuarios.COMPROBAR_USUARIO);
			pst.setString(1, usuario);
			ResultSet resultSet = pst.executeQuery();
			boolean hayRegistro = resultSet.next();
			if(hayRegistro) {
				existe = true;
			}
			else {
				existe = false;
			}
			resultSet.close();
			pst.close();
		} catch (SQLException e) {
			existe = true;
		}
		return existe;
	}
	//Esta función hace registro en la BD
	private boolean registrar(Operacion o) {
		boolean correcto = false;
		PreparedStatement pst = null;
		try {
			//Insercion del nuevo usuario y passwd
			pst = conn.prepareStatement(GestorUsuarios.INSERTAR_USUARIO);
			pst.setString(1, o.getUsuario());
			pst.setString(2, o.getPassword());
			pst.executeUpdate();
			correcto = true;
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}
	//Esta función hace login en la BD
	private boolean login(Operacion o) {
		boolean correcto = false;
		PreparedStatement pst = null;
		try {
			//Consulta con condicion de usuario y passwd
			pst = conn.prepareStatement(GestorUsuarios.COMPROBAR_LOGIN);
			pst.setString(1, o.getUsuario());
			pst.setString(2, o.getPassword());
			ResultSet resultSet = pst.executeQuery();
			//Se comprueba si el cursor tiene registros
			boolean hayRegistros = resultSet.next();
			if(hayRegistros) {
				correcto = true;
			}
			else {
				correcto = false;
				
			}
		} catch (SQLException e) {
			correcto = false;
		}
		return correcto;
	}
}
