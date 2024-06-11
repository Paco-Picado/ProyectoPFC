package com.example.appagricola.principal.sesiones

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.appagricola.enviadorPeticiones.EnviadorPeticiones
import com.example.appagricola.peticionRespuesta.Peticion
import com.example.appagricola.R
import com.example.appagricola.peticionRespuesta.RespuestaError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentoRegistro : Fragment() {

    private lateinit var botonRegistrar: Button
    private lateinit var campoUsuario: EditText
    private lateinit var campoPassword: EditText
    private lateinit var campoPasswordConfirmar: EditText
    private lateinit var enviadorPeticiones: EnviadorPeticiones
    private  lateinit var botonIrALogin: Button
    private lateinit var ibtIrAFragmentoConfiguracion: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        enviadorPeticiones = EnviadorPeticiones.getInstance(context)
        botonRegistrar = activity?.findViewById(R.id.buttonRegistro)!!
        campoUsuario = activity?.findViewById(R.id.editTextUsuarioRegistro)!!
        campoPassword = activity?.findViewById(R.id.editTextPasswordRegistro)!!
        campoPasswordConfirmar = activity?.findViewById(R.id.editTextConfirmarPassword)!!
        botonIrALogin = activity?.findViewById(R.id.buttonIrALogin)!!
        botonIrALogin.setOnClickListener{
            irALogin()
        }
        botonRegistrar.setOnClickListener{
            clickBotonRegistro()
        }
        ibtIrAFragmentoConfiguracion = activity?.findViewById(R.id.ibtIrAFragmentoConfiguracion)!!
        ibtIrAFragmentoConfiguracion.setOnClickListener {
            cambiarFragmento(ConfiguracionFragment())
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmento_registro, container, false)
    }
    private fun clickBotonRegistro(){
        if(campoUsuario.text.toString().isEmpty()){
            campoUsuario.error = "Campo usuario no puede estar vacío."
        }
        else if(campoPassword.text.toString().isEmpty()){
            campoPassword.error = "Campo contraseña no puede estar vacío."
        }
        else if(campoPassword.length() < 8){
            campoPassword.error = "La longitud de la contraseña no puede ser menor de 8 caracteres."
        }
        else if(campoPasswordConfirmar.text.toString() != campoPassword.text.toString()){
            campoPasswordConfirmar.error = "Las contraseñas deben de ser iguales."
        }
        else{
            lifecycleScope.launch(Dispatchers.IO) {
                hacerRegistro()
            }
        }
    }

    private fun hacerRegistro() {

        var p = Peticion()
        p.tipo = "Registro"
        p.password = campoPassword.text.toString()
        p.usuario = campoUsuario.text.toString()
        try {
            var respuesta = enviadorPeticiones.enviarPeticion(p)
            if(respuesta.resultado.uppercase() == "OK"){
                confirmarRegistro()
            }
            else{
                rechazarRegistro(respuesta.error)
            }
        }
        catch(e: Exception){
            //fallo conexion con servidor
            lifecycleScope.launch(Dispatchers.Main) {
                Toast.makeText(context, "No se pudo establecer conexión con el servidor.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun rechazarRegistro(e: RespuestaError) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                if(e.causa.uppercase() == "USER"){
                    campoUsuario.error = e.mensaje
                }
                else if(e.causa.uppercase() == "PASSWD"){
                    campoPassword.error = e.mensaje

                }
                else{
                    Toast.makeText(context, e.mensaje, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun confirmarRegistro(){
        irALogin()
    }
    private fun irALogin(){
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                cambiarFragmento(FragmentoLogin())
            }
        }
    }
    private fun cambiarFragmento(f: Fragment){
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frameContainerPrincipal, f)
        transaction.commit()
    }

}