package com.example.appagricola.peticionRespuesta;

import com.example.appagricola.tareas.TareasDia;

import java.util.ArrayList;

public class RespuestaServidor {
    private String resultado;
    private String sesion;
    private RespuestaError error;
    private ArrayList<TareasDia> calendario;

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public void setError(RespuestaError error) {
        this.error = error;
    }

    public void setCalendario(ArrayList<TareasDia> calendario) {
        this.calendario = calendario;
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
