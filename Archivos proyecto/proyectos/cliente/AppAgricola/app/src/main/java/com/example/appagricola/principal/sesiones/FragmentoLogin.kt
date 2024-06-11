package com.example.appagricola.principal.sesiones

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.appagricola.enviadorPeticiones.EnviadorPeticiones
import com.example.appagricola.peticionRespuesta.Peticion
import com.example.appagricola.R
import com.example.appagricola.peticionRespuesta.RespuestaError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentoLogin : Fragment(){
    private lateinit var enviadorPeticiones: EnviadorPeticiones
    private lateinit var campoPassword: EditText
    private lateinit var campoUsuario: EditText
    private lateinit var botonLogin: Button
    private lateinit var botonIrARegistro: Button
    private lateinit var ibtIrAFragmentoRegistro: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        enviadorPeticiones = EnviadorPeticiones.getInstance(context)
        ibtIrAFragmentoRegistro = activity?.findViewById(R.id.ibtIrAFragmentoRegistro)!!
        ibtIrAFragmentoRegistro.setOnClickListener {
            cambiarFragmento(FragmentoRegistro())
        }
        campoPassword = activity?.findViewById(R.id.editTextPasswordLogin)!!
        campoUsuario = activity?.findViewById(R.id.editTextUsuarioLogin)!!
        botonLogin = activity?.findViewById(R.id.buttonLogin)!!
        botonIrARegistro = activity?.findViewById(R.id.buttonIrARegistro)!!
        botonLogin.setOnClickListener {
            clickBotonLogin()
        }
        botonIrARegistro.setOnClickListener {
            irARegistro()
        }
    }

    private fun irARegistro() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                cambiarFragmento(FragmentoRegistro())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmento_login, container, false)
    }
    private fun clickBotonLogin(){
        if(campoUsuario.text.toString().isEmpty()){
            campoUsuario.error = "El campo usuario no debe de ir vacio."
        }
        else if(campoPassword.text.toString().isEmpty()){
            campoPassword.error = "El campo contraseña no debe de ir vacio."
        }
        else{
            lifecycleScope.launch(Dispatchers.IO) {
                hacerLogin()
            }
        }
    }
    private fun hacerLogin(){
        try {
            var p = Peticion()
            p.tipo = "Login"
            p.password = campoPassword.text.toString()
            p.usuario = campoUsuario.text.toString()
            var respuesta = enviadorPeticiones.enviarPeticion(p)
            if(respuesta.resultado.uppercase() == "OK"){
                confirmarLogin(respuesta.sesion, p.usuario)
            }
            else{
                rechazarLogin(respuesta.error)
            }
        }
        catch (e:Exception){
             lifecycleScope.launch(Dispatchers.Main) {
                 Toast.makeText(context, "No se pudo establecer conexión con el servidor.", Toast.LENGTH_LONG).show()
             }
        }
    }

    private fun confirmarLogin(sesion: String, usuario: String) {
        guardarSesion(sesion, usuario)
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                cambiarFragmento(ConfiguracionFragment())
            }
        }
    }
    private fun guardarSesion(sesion: String, usuario: String) {
        var preferences = context?.getSharedPreferences("acceso",Context.MODE_PRIVATE)
        var editor = preferences?.edit()
        editor?.putString("sesion", sesion)
        editor?.putString("usuario", usuario)
        editor?.apply()
    }
    private fun cambiarFragmento(f: Fragment){
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frameContainerPrincipal, f)
        transaction.commit()
    }

    private fun rechazarLogin(e: RespuestaError) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                if(e.causa.uppercase() == "USER-PASSWD"){
                    campoUsuario.error = e.mensaje
                }
                else{
                    Toast.makeText(context, e.mensaje, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}


