package com.example.appagricola.tareas;


import androidx.fragment.app.FragmentActivity;

import com.example.appagricola.baseDatos.GestorDeBaseDeDatos;

import java.util.ArrayList;

public class TareasProvider {
    public static ArrayList<Tarea> getListaTareas(FragmentActivity activity, int id, String fecha){
        GestorDeBaseDeDatos gbd = GestorDeBaseDeDatos.getInstance(activity);
        ArrayList<Tarea> listaTareas = gbd.obtenerTareasDia(id,fecha);
        return listaTareas;
    }
}
