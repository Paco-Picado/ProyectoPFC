package com.example.appagricola.tareas;

import com.example.appagricola.tareas.Tarea;

import java.util.ArrayList;

public class TareasDia {
    private String fecha;
    private ArrayList<Tarea> listaTareas;
    public TareasDia(String fecha){
        this.fecha = fecha;
        listaTareas = new ArrayList<Tarea>();
    }
    public void anhadirTarea(Tarea t){
        listaTareas.add(t);
    }
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Tarea> getListaTareas() {
        return listaTareas;
    }

    public void setListaTareas(ArrayList<Tarea> listaTareas) {
        this.listaTareas = listaTareas;
    }
}
