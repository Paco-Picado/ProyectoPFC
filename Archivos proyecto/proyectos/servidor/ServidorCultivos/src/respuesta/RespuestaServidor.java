package respuesta;

import java.util.ArrayList;

import tarea.TareasDia;

public class RespuestaServidor {
	private String resultado;
	private String sesion;
	private RespuestaError error;
	private ArrayList<TareasDia> calendario;
	public void crearRespuestaAfirmativa(String sesion, ArrayList<TareasDia> calendario) {
		resultado = "OK";
		this.sesion = sesion;
		this.calendario = calendario;
	}
	public void crearRespuestaNegativa(RespuestaError error) {
		resultado = "NOK";
		this.error = error;
	}
	public String getResultado() {
		return resultado;
	}
	public String getSesion() {
		return sesion;
	}
	public RespuestaError getError() {
		return error;
	}
	public ArrayList<TareasDia> getCalendario() {
		return calendario;
	}	
}
