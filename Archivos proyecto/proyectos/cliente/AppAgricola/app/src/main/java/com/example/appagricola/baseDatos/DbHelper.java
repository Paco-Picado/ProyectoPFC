package com.example.appagricola.baseDatos;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final  String DATABASE_NOMBRE="cultivos.db";
    private Context context;
    //DBHelper crea la base de datos y las tablas
    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            crearEInsertarDatosIniciales(db);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Esta función crea las tablas e inserta los datos iniciales a partir de un fichero personajes.sql
    private void crearEInsertarDatosIniciales(SQLiteDatabase db) throws IOException {
        //Se crean los lectores del archivo
        String[] lineaSQL = devolverLineasDeArchivo();
        //Se ejecutan las lineas de sql
        for(int i=0;i< lineaSQL.length;++i){
            db.execSQL(lineaSQL[i]);
        }
    }
    //Esta función lee el archivo sql y devuelve las lineas
    @NonNull
    private String[] devolverLineasDeArchivo() throws IOException {
        //Se busca el fichero de la carpeta asseets, se lee y se devuelven las lineas a ejecutar
        AssetManager assetManager = context.getAssets();
        InputStream inputStream =  assetManager.open("cultivos_app.sql");
        BufferedReader lector = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String linea;
        while((linea = lector.readLine())!=null){
            sb.append(linea);
        }
        //El ; indica el final del comando sql, se hace split por ;
        String[] lineaSQL = sb.toString().split(";");
        return lineaSQL;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Este método no se usa pero tiene que ser implementando obligatoriamente
        //al extender de SQLiteOpenHelper
    }
}
