package com.example.appagricola.cultivos;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Cultivo {
    public enum Fase{
        Germinacion("germinacion"),
        Floracion("floracion"),
        Desarrollo("desarrollo"),
        Maduracion("maduracion");

        private String fase;
        Fase(String fase) {
            this.fase = fase;
        }
        public String toString() {
            return fase;
        }
    }
    private int id;
    private String nombre;
    private String alias;
    private Fase fase;
    public Cultivo(String nombre, String alias, Fase fase){
        this.nombre = nombre;
        this.alias = alias;
        this.fase = fase;
    }
    public Cultivo(){

    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(String fase) {
        fase = fase.toLowerCase();
        for(Fase f: Fase.values()){
            if(fase.equalsIgnoreCase(f.name())){
                this.fase = f;
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    public Fase cambiarDeFase(){
        if (fase == Fase.Germinacion){
            fase = Fase.Desarrollo;
        }
        else if (fase == Fase.Desarrollo) {
            fase = Fase.Floracion;
        }
        else if (fase == Fase.Floracion) {
            fase = Fase.Maduracion;
        }
        //La última fase se mantiene igual
        return fase;
    }
}
