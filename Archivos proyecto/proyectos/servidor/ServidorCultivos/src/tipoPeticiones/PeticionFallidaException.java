package tipoPeticiones;

import respuesta.RespuestaError;

public class PeticionFallidaException extends Exception {	

	private RespuestaError error;
	public PeticionFallidaException(String[] mensajeError) {
		error = new RespuestaError(mensajeError);
	}	
	public RespuestaError getError() {
		return error;
	}
	@Override
	public String getMessage() {
		return error.getMensaje();
	}
	
}
