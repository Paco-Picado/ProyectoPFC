package servidor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

import conector.ConectorBDCultivos;
import gestionUsuarios.GestorSesiones;
import gestionUsuarios.GestorUsuarios;


public class Servidor {
	private static SSLServerSocket serverSocket;
	private static ConectorBDCultivos conector;
	private static GestorUsuarios gestor;
	private static GestorSesiones gestorSesiones;
	public static void main(String[] args) {
		serverSocket = getServerSocket();
		SSLSocket socket;
		conector = new ConectorBDCultivos();
		gestor = new GestorUsuarios();
		gestorSesiones = new GestorSesiones();
		while(true) {
			try {
				socket = (SSLSocket) serverSocket.accept();
				Thread h = new Thread(new HiloPeticion(socket, conector, gestor, gestorSesiones));
				h.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private static  SSLServerSocket getServerSocket() {
		SSLServerSocket serverSocket = null;
		try {
			KeyStore almacen = KeyStore .getInstance("pkcs12");
			InputStream is = Servidor.class.getResourceAsStream("clave/clavePrivada.pk12"); //Necesario para exportar a jar
			almacen.load(is, "123qwe".toCharArray());
			//almacen.load(new FileInputStream("clave/clavePrivada.pk12"), "123qwe".toCharArray());
			SSLContext context = SSLContext.getInstance("TLS");
			KeyManagerFactory km = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());		
			km.init(almacen, "123qwe".toCharArray());
			context.init(km.getKeyManagers(), null, null);
			serverSocket = (SSLServerSocket) context.getServerSocketFactory().createServerSocket(5000);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverSocket;
	}

}
