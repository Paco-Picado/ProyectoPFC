package com.example.appagricola.enviadorPeticiones;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.example.appagricola.peticionRespuesta.Peticion;
import com.example.appagricola.peticionRespuesta.RespuestaServidor;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class EnviadorPeticiones {
    private static EnviadorPeticiones enviador;
    private Context context;
    private SSLSocket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private InetAddress ip;
    private int puerto = 5000;

    public boolean setDir(String ip) {
        boolean correcto = true;
        try {
            this.ip = InetAddress.getByName(ip);
        }
        catch(Exception e){
            correcto  = false;
        }
        return correcto;
    }
    private void abrirSocket() {
        try {
            this.socket = getSocket();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private  SSLSocket getSocket() {
        SSLSocket socket = null;
        try {
            KeyStore almacen = KeyStore.getInstance(KeyStore.getDefaultType());
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open("clave/almacen.bks");
            almacen.load(is, "123456".toCharArray());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory tm = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tm.init(almacen);
            sslContext.init(null, tm.getTrustManagers(), null);
            socket = (SSLSocket) sslContext.getSocketFactory().createSocket(ip, puerto);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return socket;
    }
    private void cerrarSocket(){
        try {
            br.close();
            bw.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static EnviadorPeticiones getInstance(Context context){
        if(enviador == null){
            enviador = new EnviadorPeticiones(context);
        }
        return enviador;
    }
    private EnviadorPeticiones(Context context){
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences("dir", Context.MODE_PRIVATE);
        setDir(preferences.getString("ip", "localhost"));
    }
    private void enviarMensaje(String mensaje){
        try {
            bw.write(mensaje);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String recibirMensaje(){
        String mensaje = null;
        try {
            mensaje = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  mensaje;
    }

    public RespuestaServidor enviarPeticion(Peticion p) {
        abrirSocket();
        Gson gson = new Gson();
        String mensaje = gson.toJson(p);
        enviarMensaje(mensaje);
        String respuesta = recibirMensaje();
        cerrarSocket();
        return  gson.fromJson(respuesta, RespuestaServidor.class);
    }
}
