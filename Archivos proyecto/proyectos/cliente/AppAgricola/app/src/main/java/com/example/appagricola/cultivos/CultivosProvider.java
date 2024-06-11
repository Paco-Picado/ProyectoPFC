package com.example.appagricola.cultivos;


import androidx.fragment.app.FragmentActivity;

import com.example.appagricola.baseDatos.GestorDeBaseDeDatos;

import java.util.ArrayList;

public class CultivosProvider {
    public static ArrayList<Cultivo> getListaCultivos(FragmentActivity activity){
        GestorDeBaseDeDatos gbd = GestorDeBaseDeDatos.getInstance(activity);
        ArrayList<Cultivo> listaCultivos = gbd.buscarCultivos();
        return listaCultivos;
    }
}
