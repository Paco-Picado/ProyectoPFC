package com.example.appagricola.tareasActivity.calendario;

public class Dia {
    private String fecha;
    private boolean hayTareas;

    public Dia(String fecha) {
        this.fecha = fecha;
        hayTareas = false;
    }
    public Dia(){}

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isHayTareas() {
        return hayTareas;
    }

    public void setHayTareas(boolean hayTareas) {
        this.hayTareas = hayTareas;
    }
}
