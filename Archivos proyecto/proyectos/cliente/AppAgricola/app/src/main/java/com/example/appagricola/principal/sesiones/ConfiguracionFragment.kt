package com.example.appagricola.principal.sesiones

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.appagricola.enviadorPeticiones.EnviadorPeticiones
import com.example.appagricola.peticionRespuesta.Peticion
import com.example.appagricola.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfiguracionFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuracion, container, false)
    }
    private lateinit var btBotonSesiones: Button
    private lateinit var btBotonEstablecerIPyPuerto: Button
    private lateinit var tvUsuario: TextView
    private lateinit var etIPServidor: EditText
    override fun onStart() {
        super.onStart()
        btBotonSesiones = activity?.findViewById(R.id.btnSesion)!!
        btBotonEstablecerIPyPuerto = activity?.findViewById(R.id.btnCambiarIP)!!
        btBotonEstablecerIPyPuerto.setOnClickListener {
            clickBotonEstablecerIP()
        }
        etIPServidor = activity?.findViewById(R.id.etIPServidor)!!
        etIPServidor.setText(context?.getSharedPreferences("dir",
            Context.MODE_PRIVATE)?.getString("ip","localhost"))

        tvUsuario = activity?.findViewById(R.id.tvNombreUsuario)!!
        val sesion  = context?.getSharedPreferences("acceso", Context.MODE_PRIVATE)?.
                                                    getString("sesion","NO")
        if(sesion == "NO"){
            tvUsuario.text = "No has iniciado sesión"
            btBotonSesiones.text = "Registrarse"
            btBotonSesiones.setOnClickListener {
                cambiarAFragmentoRegistro()
            }
        }
        else{
            btBotonSesiones.text = "Cerrar sesión"
            tvUsuario.text = context?.getSharedPreferences("acceso", Context.MODE_PRIVATE)?.getString("usuario", "NO")
            btBotonSesiones.setOnClickListener {
                cerrarSesion()
            }
        }
    }
    private fun cerrarSesion(){
            lifecycleScope.launch{
                withContext(Dispatchers.IO) {
                    try {
                        val enviadorPeticiones = EnviadorPeticiones.getInstance(context)
                        val p = Peticion()
                        p.tipo = "cerrar"
                        p.sesion = context?.getSharedPreferences("acceso", Context.MODE_PRIVATE)
                            ?.getString("sesion", "NO")
                        Log.i("SESION", p.sesion)
                        val r = enviadorPeticiones.enviarPeticion(p)
                        Log.i("RESULTADO", r.resultado)
                        var editor =
                            context?.getSharedPreferences("acceso", Context.MODE_PRIVATE)?.edit()
                        editor?.remove("sesion")
                        editor?.remove("usuario")
                        editor?.apply()
                    }
                    catch (e:Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "No se pudo establecer conexión con el servidor.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                cambiarAFragmentoRegistro()
            }
    }
    private fun clickBotonEstablecerIP(){
        var ip = etIPServidor.text.toString()
        if(EnviadorPeticiones.getInstance(context).setDir(ip)){
            Toast.makeText(context,"La ip es correcta", Toast.LENGTH_SHORT).show()
            val editor = context?.getSharedPreferences("dir",Context.MODE_PRIVATE)?.edit()
            editor?.putString("ip", ip)
            editor?.apply()

        }
        else{
            etIPServidor.error = "La ip no es válida"
        }
    }
    private fun cambiarAFragmentoRegistro(){
        cambiarFragmento(FragmentoRegistro())
    }
    private fun cambiarFragmento(f: Fragment){
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frameContainerPrincipal, f)
        transaction.commit()
    }
}