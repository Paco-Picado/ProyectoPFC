package com.example.appagricola.peticionRespuesta;

public class Peticion {
    private String tipo;
    private String usuario;
    private String password;
    private String sesion;
    private String cultivo;
    private String fase;
    private PeticionProblema problema;
    //private String mail;
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCultivo() {
        return cultivo;
    }
    public void setCultivo(String cultivo) {
        this.cultivo = cultivo;
    }

    public PeticionProblema getProblema() {
        return problema;
    }

    public void setProblema(PeticionProblema problema) {
        this.problema = problema;
    }

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }
}
