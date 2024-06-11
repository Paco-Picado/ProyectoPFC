package com.example.appagricola.peticionRespuesta;

public class RespuestaError {
    private String causa;
    private String mensaje;
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
