package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import calendario.CreadorDeCalendario;
import conector.ConectorBDCultivos;
import gestionUsuarios.GestorSesiones;
import gestionUsuarios.GestorUsuarios;
import gestionUsuarios.Operacion;
import respuesta.RespuestaError;
import respuesta.RespuestaServidor;
import sistemaExperto.SistemaExpertoHongos;
import sistemaExperto.SistemaExpertoInsectos;
import gestionUsuarios.Sesion;
import tarea.TareasBD;
import tarea.TareasDia;
import tipoPeticiones.Peticion;
import tipoPeticiones.PeticionFallidaException;
import tipoPeticiones.PeticionProblema;

public class HiloPeticion implements Runnable {
	public enum TipoDePeticion {Registro, Login, Cultivo, Problema, CerrarSesion, Desconocida}
	private SSLSocket socket;	
	private BufferedReader br;
	private BufferedWriter bw;
	private ConectorBDCultivos conector;
	private Peticion peticionCliente;
	private GestorUsuarios gestorUsuarios;
	private GestorSesiones gestorSesiones;
	public HiloPeticion(SSLSocket socket, ConectorBDCultivos conector, GestorUsuarios gestor, GestorSesiones gestorSesiones) {
		this.socket = socket;
		this.conector = conector;
		this.gestorUsuarios = gestor;
		this.gestorSesiones = gestorSesiones;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		RespuestaServidor respuestaServidor = null;
		try {
			String peticion = br.readLine();
			TipoDePeticion tipo = interpretarPeticion(peticion);
			respuestaServidor = ejecutarPeticion(tipo);
			if(respuestaServidor == null) {
				throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
			}
		} catch (Exception e) {
			respuestaServidor = new RespuestaServidor();
			respuestaServidor.crearRespuestaNegativa(new RespuestaError(RespuestaError.FALLO_DESCONOCIDO));
		}
		enviarMensaje(respuestaServidor);
	}
	//Esta función lee e interpreta la petición
	private TipoDePeticion interpretarPeticion(String peticion) throws PeticionFallidaException {
		TipoDePeticion tipo = null;
		Gson gson = new Gson();
		peticionCliente = gson.fromJson(peticion, Peticion.class);
		//REGISTRO
		if(Peticion.REGISTRO.equals(peticionCliente.getTipo().toLowerCase())) {
			tipo = TipoDePeticion.Registro;								
		}
		//LOGIN
		else if(Peticion.LOGIN.equals(peticionCliente.getTipo().toLowerCase())) {
			tipo = TipoDePeticion.Login;				
		}
		//CULTIVO
		else if(Peticion.CULTIVO.equals(peticionCliente.getTipo().toLowerCase())) {
			tipo = TipoDePeticion.Cultivo;				
		}
		//PROBLEMA PLAGA Y HONGOS
		else if(Peticion.PROBLEMA.equals(peticionCliente.getTipo().toLowerCase())) {
			tipo = TipoDePeticion.Problema;
		}
		//CERRAR SESIÓN
		else if(Peticion.CERRAR_SESION.equals(peticionCliente.getTipo().toLowerCase())) {
			tipo = TipoDePeticion.CerrarSesion;
		}
		//DESCONOCIDA
		else {
			tipo = TipoDePeticion.Desconocida;
		}
		return tipo;		
	}
	//Esta función ejecuta la petición
	private RespuestaServidor ejecutarPeticion(TipoDePeticion tipo) throws PeticionFallidaException {
		RespuestaServidor respuestaServidor = new RespuestaServidor();
		try {
			if(tipo == TipoDePeticion.Registro) {
				respuestaServidor = iniciarRegistro();
			}
			else if(tipo == TipoDePeticion.Login) {
				respuestaServidor = iniciarLogin();
			}
			else if(tipo == TipoDePeticion.Cultivo) {
				respuestaServidor = iniciarPeticionCultivo();
			}
			else if(tipo == TipoDePeticion.Problema) {
				respuestaServidor = iniciarPeticionProblema();
			}
			else if(tipo == TipoDePeticion.CerrarSesion) {
				respuestaServidor = iniciarCierreSesion();
			}
			else {
				throw new PeticionFallidaException(RespuestaError.PETICION_DESCONOCIDA);
			}
		}
		catch(PeticionFallidaException e) {
			respuestaServidor.crearRespuestaNegativa(e.getError());
		}
		catch(JsonSyntaxException e) {
			respuestaServidor.crearRespuestaNegativa(new RespuestaError(RespuestaError.PROTOCOLO_ERROR));
		}
		return respuestaServidor;
	}
	private RespuestaServidor iniciarCierreSesion() throws PeticionFallidaException, JsonSyntaxException {
		RespuestaServidor respuestaServidor = new RespuestaServidor();
		Sesion sesion = new Sesion();
		sesion.crearSolicitudCerrarSesion(peticionCliente.getSesion());
		gestorSesiones.gestionarSesion(sesion);
		respuestaServidor.crearRespuestaAfirmativa(null, null);
		return respuestaServidor;
	}
	private RespuestaServidor iniciarPeticionProblema() throws PeticionFallidaException, JsonSyntaxException {
		RespuestaServidor respuestaServidor = new RespuestaServidor();
		Sesion sesion = new Sesion();
		sesion.crearSolicitudAcceso(peticionCliente.getUsuario(), peticionCliente.getSesion());
		gestorSesiones.gestionarSesion(sesion);
		if(peticionCliente.getProblema() != null && peticionCliente.getCultivo() != null) {
			ArrayList<TareasBD> tareasBD = new ArrayList<TareasBD>();
			//Sistema Experto Hongos
			PeticionProblema p = peticionCliente.getProblema();
			SistemaExpertoHongos seh = new SistemaExpertoHongos();
			seh.setHumedad(p.getHumedad());
			seh.setManchas(p.getManchasHojas());
			seh.setMoho(p.getMoho());
			seh.setPodrido(p.getPodrido());
			seh.aplicarSistemaExperto();
			boolean hayHongos =  seh.getResultado();
			if(hayHongos) {
				tareasBD = conector.devolverTareasCultivo(peticionCliente.getCultivo(), SistemaExpertoHongos.PROBLEMA,
						ConectorBDCultivos.PROBLEMA_CULTIVO);
			}
			//Sistema Experto Insectos
			SistemaExpertoInsectos sei = new SistemaExpertoInsectos();
			sei.setAgujerosFrutos(p.getAgujerosFrutos());
			sei.setAgujerosHojas(p.getAgujerosHojas());
			sei.setAgujerosTallo(p.getAgujerosTallo());
			sei.setInsectosCerca(p.getInsectos());
			sei.aplicarSistemaExperto();
			boolean hayPlaga = sei.getResultado();
			if(hayPlaga) {
				ArrayList<TareasBD> listaTareasPlaga = conector.devolverTareasCultivo(peticionCliente.getCultivo(),SistemaExpertoInsectos.PROBLEMA,
						ConectorBDCultivos.PROBLEMA_CULTIVO);
				tareasBD.addAll(listaTareasPlaga);
			}
			CreadorDeCalendario cc = new CreadorDeCalendario();
			cc.crearCalendario(tareasBD);
			ArrayList<TareasDia> calendario = cc.getCalendario();
			respuestaServidor.crearRespuestaAfirmativa(null, calendario);
		}
		else {
			throw new PeticionFallidaException(RespuestaError.FALLO_DESCONOCIDO);
		}
		return respuestaServidor;
	}
	//Esta función inicia el proceso de registro
	private RespuestaServidor iniciarRegistro() throws PeticionFallidaException, JsonSyntaxException {
		RespuestaServidor respuestaServidor = null;		
		Operacion o = new Operacion();
		o.crearOperacionRegistro(peticionCliente.getUsuario(), peticionCliente.getPassword());
		if(o.getPassword() == null){
			//la password no cumple los requisitos
			throw new PeticionFallidaException(RespuestaError.REG_PASSWD_NO_VALIDA);				
		}
		else if(o.getUsuario() == null) {
			throw new PeticionFallidaException(RespuestaError.REG_USUARIO_EXISTE);				
		}
		else {
			gestorUsuarios.hacerOperacionConUsuario(o);
			respuestaServidor = new RespuestaServidor();
			respuestaServidor.crearRespuestaAfirmativa(null, null);
		}
		return respuestaServidor;
	}
	//Esta función inicia el proceso de login
	private RespuestaServidor iniciarLogin() throws PeticionFallidaException, JsonSyntaxException{
		RespuestaServidor respuestaServidor = null;
		Operacion o = new Operacion();
		o.crearOperacionLogin(peticionCliente.getUsuario(), peticionCliente.getPassword());
		if(o.getPassword() != null && o.getUsuario() != null){
			gestorUsuarios.hacerOperacionConUsuario(o);
			Sesion sesion = new Sesion();
			sesion.crearSolicitudNueva(o.getUsuario());
			sesion = gestorSesiones.gestionarSesion(sesion);
			respuestaServidor = new RespuestaServidor();
			respuestaServidor.crearRespuestaAfirmativa(sesion.getUuid(), null);
			
		}
		else {
			//USER Y PASSWD INCORRECTOS
			throw new PeticionFallidaException(RespuestaError.LOG_PASSWD_USER);
		}
		return respuestaServidor;
		
	}
	//Esta función inicia el proceso peticion cultivo
	private RespuestaServidor iniciarPeticionCultivo() throws PeticionFallidaException {
		RespuestaServidor respuestaServidor = new RespuestaServidor();
		ArrayList<TareasBD> tareasBD = 
				conector.devolverTareasCultivo(peticionCliente.getCultivo(),
						peticionCliente.getFase(), ConectorBDCultivos.CULTIVO);		
		CreadorDeCalendario cc = new CreadorDeCalendario();
		cc.crearCalendario(tareasBD);
		ArrayList<TareasDia> calendario = cc.getCalendario();
		respuestaServidor.crearRespuestaAfirmativa(null, calendario);
		return respuestaServidor;
	}
	private void enviarMensaje(RespuestaServidor respuestaServidor) {
		try {
			Gson gson = new Gson();
			String mensaje = gson.toJson(respuestaServidor);
			bw.write(mensaje);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
