package gestionUsuarios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import gestionUsuarios.Sesion.SolicitudSesion;
import respuesta.RespuestaError;
import tipoPeticiones.PeticionFallidaException;

public class GestorSesiones {
	private static final String BUSCAR_UUID = "select * from sesiones where uuid = ?";
	private static final String INSERTAR_SESION = "insert into sesiones(uuid, usuario) values (?,?)";
	private static final String BORRAR_SESION = "delete from sesiones where uuid = ?";
	private Connection conn;
	//Esta clase permite acceder a la base de datos de las sesiones de usuario
	private void abrirConexion() throws PeticionFallidaException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_sesiones","root","123qwe");
		} catch (Exception e) {
			throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
		}	
	}
	public synchronized Sesion gestionarSesion(Sesion sesion) throws PeticionFallidaException {
		abrirConexion();
		if(sesion.getSolicitud() == SolicitudSesion.Nueva) {
			String uuid = anhadirUnaSesion(sesion.getUsuario());
			sesion.setUuid(uuid);
		}
		else if(sesion.getSolicitud() == SolicitudSesion.Acceso) {
			if(!existeUUID(sesion.getUuid())) {
				//Acceso Incorrecto
				throw new PeticionFallidaException(RespuestaError.SESION_ERROR);
			}
		}
		else if(sesion.getSolicitud() == SolicitudSesion.Quitar){
			cerrarSesion(sesion.getUuid());
		}
		cerrarConexion();
		return sesion;
	}
	private String anhadirUnaSesion(String usuario) throws PeticionFallidaException {
		String uuid = null;
		do {
			uuid = UUID.randomUUID().toString();
		}while(existeUUID(uuid));
		try {
			PreparedStatement pst = conn.prepareStatement(INSERTAR_SESION);
			pst.setString(1, uuid);
			pst.setString(2, usuario);
			pst.executeUpdate();			
		} catch (SQLException e) {
			throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
		}
		return uuid;
	}
	private boolean existeUUID(String uuid) {
		PreparedStatement pst = null;
		boolean existe = true;
		try {
			pst = conn.prepareStatement(BUSCAR_UUID);
			pst.setString(1, uuid);
			ResultSet resultSet = pst.executeQuery();
			existe = resultSet.next();
		} catch (SQLException e) {
			existe = false;
		}
		return existe;
	}
	private void cerrarSesion(String uuid) throws PeticionFallidaException {
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(BORRAR_SESION);
			pst.setString(1, uuid);
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO); 
		}
	}
	private void cerrarConexion() throws PeticionFallidaException {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
		}
	}
	
}
