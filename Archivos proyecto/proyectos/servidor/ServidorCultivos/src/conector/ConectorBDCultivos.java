package conector;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import respuesta.RespuestaError;
import tarea.Tarea;
import tarea.TareasBD;
import tipoPeticiones.PeticionFallidaException;

public class ConectorBDCultivos {
	public static final String CULTIVO = "select ta.nombre,ta.dia_inicial,ta.dia_final,ta.intervalo_dias, te.texto\r\n" + 
			"from tareas ta\r\n" + 
			"join textos te\r\n" + 
			"	on te.id = ta.id_textos\r\n" + 
			"join lista_tareas la\r\n" + 
			"		on la.id = ta.id_lista\r\n" + 
			"join fases_cultivos fc\r\n" + 
			"	on fc.id = la.id_fase\r\n" + 
			"join cultivos cu\r\n" + 
			"	on cu.id=fc.id_cultivo\r\n" + 
			"where cu.nombre = ?\r\n" + 
			"	and fc.descripcion = ?\r\n" + 
			"order by ta.dia_inicial, ta.dia_final";
	public static final String PROBLEMA_CULTIVO = "select tap.nombre,tap.dia_inicial,tap.dia_final,tap.intervalo_dias, te.texto\r\n" + 
			"from tareas_problemas tap\r\n" + 
			"join textos te\r\n" + 
			"	on te.id = tap.id_textos\r\n" + 
			"join lista_tareas_problemas lap\r\n" + 
			"		on lap.id = tap.id_lista\r\n" + 
			"join problemas_cultivos pc\r\n" + 
			"	on pc.id = lap.id_problemas_cultivos\r\n" + 
			"join cultivos cu\r\n" + 
			"	on cu.id= pc.id_cultivo\r\n" + 
			"where cu.nombre = ?\r\n" + 
			"	and pc.descripcion = ?\r\n" + 
			"order by tap.dia_inicial, tap.dia_final";
	private Connection conn;
	private void abrirConexion() throws PeticionFallidaException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_cultivos","root","123qwe");
		} catch (Exception e) {
			System.out.println("No abrió");
			throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
		}
	}
	//Devuelve una lista de tareas en formato TareaBD
	public synchronized ArrayList<TareasBD> devolverTareasCultivo(String cultivo, String faseProblema, String consulta) throws PeticionFallidaException{
		abrirConexion();
		ArrayList<TareasBD> listaTareasBD = new ArrayList<TareasBD>();
		ResultSet cursor = devolverCursor(cultivo, faseProblema, consulta);
		TareasBD tareaBD;
		try {
			while(cursor.next()) {
				tareaBD = construirTareaBD(cursor);
				listaTareasBD.add(tareaBD);
			}
			cursor.close();
		} catch (SQLException e) {
			throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
		}
		cerrarConexion();
		return listaTareasBD;
	}
	private void cerrarConexion() throws PeticionFallidaException {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
		}
		
	}
	//devuelve un cursor con los datos de la bd a partir de un cultivo y un problema
	private synchronized ResultSet devolverCursor(String cultivo, String faseProblema, String consulta) throws PeticionFallidaException {
		ResultSet cursor = null;
		try {
			PreparedStatement pst = conn.prepareStatement(consulta);
			pst.setString(1, cultivo);
			pst.setString(2, faseProblema);
			cursor = pst.executeQuery();
		} catch (SQLException e) {
			System.out.println("fallo");
			throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
		}
		return cursor;
	}
	//Construye una TareaBD a partir del cursor
	private TareasBD construirTareaBD(ResultSet cursor) throws PeticionFallidaException {
		TareasBD tareaBD = new TareasBD();
		try {
			Tarea tarea = new Tarea(cursor.getNString("nombre"), cursor.getNString("texto"));
			tareaBD.setTarea(tarea);
			tareaBD.setDiaInicial(cursor.getInt("dia_inicial"));
			tareaBD.setDiaFinal(cursor.getInt("dia_final"));
			tareaBD.setIntervaloDias(cursor.getInt("intervalo_dias"));
		} catch (SQLException e) {
			throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
		}
		return tareaBD;
	}
}
