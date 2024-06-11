package com.example.appagricola.baseDatos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.appagricola.cultivos.Cultivo;
import com.example.appagricola.tareas.Tarea;
import com.example.appagricola.tareas.TareasDia;

import java.util.ArrayList;


public class GestorDeBaseDeDatos {
    private static GestorDeBaseDeDatos gestorDeBaseDeDatos;
    private DbHelper dbHelper;
    public static GestorDeBaseDeDatos getInstance(FragmentActivity activity){
        if(gestorDeBaseDeDatos == null){
            gestorDeBaseDeDatos = new GestorDeBaseDeDatos(activity);
        }
        return gestorDeBaseDeDatos;
    }
    private final String CAMBIAR_FASE = "update cultivos set fase = ? where id = ?";
    private final String BUSCAR_TODOS_LOS_CULTIVOS = "SELECT * FROM CULTIVOS";
    private final String BUSCAR_CULTIVO = "Select * from cultivos where id = ?";
    private final String BUSCAR_TAREAS_FECHA =
            "select ta.id,ta.nombre,ta.texto\n" +
            "from tareas ta\n" +
            "join dias di\n" +
            "on di.id = ta.id_dia\n" +
            "join cultivos cu\n" +
            "on cu.id = di.id_cultivo\n" +
            "where cu.id = ?\n" +
            "and di.fecha = ?";
    private final String BUSCAR_FECHA = "select * from dias where id_cultivo = ? and fecha = ?";
    private final String INSERTAR_CULTIVO = "insert into cultivos(nombre, fase, alias) values(?,?,?)";
    private final  String BUSCAR_ALIAS = "select * from cultivos where lower(alias) = lower(?)";
    private final String BUSCAR_TAREAS_MES =
            "select di.fecha \n" +
            "from dias di\n" +
            "join tareas ta\n" +
            "on di.id = ta.id_dia\n" +
            "where di.id_cultivo = ?\n" +
            "and di.fecha like ?";
    private final String BORRAR_TAREAS_DE_UN_CULTIVO =
            "delete from tareas where id_dia in (" +
            "select di.id\n" +
            "from dias di\n" +
            "join cultivos cu\n" +
            "on cu.id = di.id_cultivo\n" +
            "where di.id_cultivo = ?)";
    private final String BORRAR_TAREA = "delete from tareas where id = ?";
    private final String INSERTAR_FECHA = "INSERT INTO dias (fecha, id_cultivo) VALUES (?, ?)";
    private final String INSERTAR_TAREA = "INSERT INTO tareas (nombre, texto, id_dia) VALUES (?, ?, (select id from dias where fecha = ? and id_cultivo = ?))";
    private final String BORRAR_DIAS_CULTIVO = "delete from dias where id_cultivo = ?";
    private final String BORRAR_CULTIVO ="delete from cultivos where id = ?";
    private SQLiteDatabase db;
    private GestorDeBaseDeDatos(FragmentActivity actividad){
        dbHelper = new DbHelper(actividad);
    }
    private void abrirBD(){
        db = dbHelper.getWritableDatabase();
    }
    private void cerrarBD(){
        db.close();
    }
    public ArrayList<Cultivo> buscarCultivos(){
        //Esta función obtiene todos los cultivos actuales de la BD
        abrirBD();
        ArrayList<Cultivo>listaCultivos = new ArrayList<Cultivo>();
        Cultivo c;
        Cursor cursor = construirConsulta(BUSCAR_TODOS_LOS_CULTIVOS, null);
        while(cursor.moveToNext()){
            c = new Cultivo();
            int pId = cursor.getColumnIndex("id");
            c.setId(cursor.getInt(pId));
            int pNombre = cursor.getColumnIndex("nombre");
            c.setNombre(cursor.getString(pNombre));
            //
            int pFase = cursor.getColumnIndex("fase");
            c.setFase(cursor.getString(pFase));
            int pAlias = cursor.getColumnIndex("alias");
            c.setAlias(cursor.getString(pAlias));
            listaCultivos.add(c);
        }
        cursor.close();
        cerrarBD();
        return listaCultivos;
    }
    public ArrayList<Tarea> obtenerTareasDia(int id, String fecha){
        abrirBD();
        //Obtener las tareas para un dia en concreto
        ArrayList<Tarea> listaTareas = new ArrayList<>();
        Tarea t;
        Cursor cursor = construirConsulta(BUSCAR_TAREAS_FECHA, new String[]{String.valueOf(id),fecha});
        while (cursor.moveToNext()){
            int pId = cursor.getColumnIndex("id");
            int pNombre = cursor.getColumnIndex("nombre");
            int pTexto = cursor.getColumnIndex("texto");
            t = new Tarea(cursor.getString(pNombre), cursor.getString(pTexto));
            t.setId(cursor.getInt(pId));
            listaTareas.add(t);
        }
        cursor.close();
        cerrarBD();
        return listaTareas;
    }
    public boolean insertarCultivo(Cultivo c){
        boolean insertado = false;
        abrirBD();
        if(!existeAlias(c.getAlias())){
            //Insertar cultivo cuando no existe otro alias igual
            SQLiteStatement st = db.compileStatement(INSERTAR_CULTIVO);
            st.bindString(1,c.getNombre());
            st.bindString(2,c.getFase().name());
            st.bindString(3, c.getAlias());
            st.executeInsert();
            st.close();
            insertado = true;
            Log.i("NUEVO CULTIVO","INSERTADO");
        }
        cerrarBD();
        return insertado;
    }
    private boolean existeAlias(String alias){
        boolean existe;
        Cursor cursor = construirConsulta(BUSCAR_ALIAS,new String[]{alias});
        if(cursor.moveToNext()){
            //Si hay registros el alias existe
            existe = true;
        }
        else{
            existe = false;
        }
        cursor.close();
        return existe;
    }
    public ArrayList<String> buscarTareasDelMes(int id, String fecha){
        abrirBD();
        ArrayList<String> fechasConTareas = new ArrayList<String>();
        Cursor cursor = construirConsulta(BUSCAR_TAREAS_MES, new String[]{String.valueOf(id),fecha});
        while(cursor.moveToNext()){
            int pFecha = cursor.getColumnIndex("fecha");
            String[] fechaTrozos =  cursor.getString(pFecha).split("/");
            fechasConTareas.add(fechaTrozos[0]);
        }
        cursor.close();
        cerrarBD();
        return fechasConTareas;
    }
    public void eliminarCultivo(int id){
        abrirBD();
        //Borrar tareas
        SQLiteStatement st = db.compileStatement(BORRAR_TAREAS_DE_UN_CULTIVO);
        st.bindLong(1, id);
        st.executeUpdateDelete();
        //Borrar dias
        st = db.compileStatement(BORRAR_DIAS_CULTIVO);
        st.bindLong(1, id);
        st.executeUpdateDelete();
        //Borrar cultivos
        st = db.compileStatement(BORRAR_CULTIVO);
        st.bindLong(1, id);
        st.executeUpdateDelete();
        st.close();
        cerrarBD();
    }
    public void reemplazarTareas(ArrayList<TareasDia> calendario, int id){
        abrirBD();
        //Borrar tareas
        SQLiteStatement st = db.compileStatement(BORRAR_TAREAS_DE_UN_CULTIVO);
        st.bindLong(1, id);
        st.executeUpdateDelete();
        //Borrar dias
        st = db.compileStatement(BORRAR_DIAS_CULTIVO);
        st.bindLong(1, id);
        st.executeUpdateDelete();
        st.close();
        cerrarBD();
        insertarNuevasTareas(calendario, id);
    }
    public void eliminarTarea(int id){
        abrirBD();
        SQLiteStatement st = db.compileStatement(BORRAR_TAREA);
        st.bindLong(1,id);
        st.executeUpdateDelete();
        st.close();
        cerrarBD();
    }
    public void insertarNuevasTareas(ArrayList<TareasDia> calendario, int id){
        abrirBD();
        for (TareasDia d: calendario){
            String fecha = d.getFecha();
            if(!comprobarSiFechaExiste(id, fecha)){
                //Insertar fecha si no hay dias para ese cultivo
                insertarDia(fecha, id);
            }
            ArrayList<Tarea> tareasDia = d.getListaTareas();
            for(Tarea t: tareasDia){
                insertarTarea(t, fecha, id);
            }
        }
        cerrarBD();
    }
    private boolean comprobarSiFechaExiste(int id, String fecha){
        boolean existe;
        Cursor cursor = construirConsulta(BUSCAR_FECHA, new String[]{String.valueOf(id),fecha});
        existe = cursor.moveToNext(); //Si hay valores existe la fecha
        return existe;
    }

    private void insertarTarea(Tarea t, String fecha, int id) {
        SQLiteStatement st = db.compileStatement(INSERTAR_TAREA);
        st.bindString(1,t.getNombre());
        st.bindString(2, t.getDescripcion());
        st.bindString(3, fecha);
        st.bindLong(4,id);
        st.executeInsert();
        st.close();
    }

    private void insertarDia(String fecha, int id){
        SQLiteStatement st = db.compileStatement(INSERTAR_FECHA);
        st.bindString(1,fecha);
        st.bindLong(2,id);
        st.executeInsert();
        st.close();
    }
    private Cursor construirConsulta(String sql, String[] param){
        Cursor cursor = db.rawQuery(sql, param);
        return cursor;
    }
    public Cultivo buscarCultivo(int id){
        Cultivo c = new Cultivo();
        abrirBD();
        Cursor cursor = construirConsulta(BUSCAR_CULTIVO, new String[]{String.valueOf(id)});
        cursor.moveToNext();
        int pId = cursor.getColumnIndex("id");
        c.setId(cursor.getInt(pId));
        int pNombre = cursor.getColumnIndex("nombre");
        c.setNombre(cursor.getString(pNombre));
        int pAlias = cursor.getColumnIndex("alias");
        c.setAlias(cursor.getString(pAlias));
        int pFase = cursor.getColumnIndex("fase");
        c.setFase(cursor.getString(pFase));
        cerrarBD();
        return c;
    }
    public void cambiarFaseCultivo(Cultivo c){
        abrirBD();
        SQLiteStatement st = db.compileStatement(CAMBIAR_FASE);
        st.bindString(1,c.getFase().name());
        st.bindLong(2,c.getId());
       int columnas = st.executeUpdateDelete();
        st.close();
        cerrarBD();
    }
}
