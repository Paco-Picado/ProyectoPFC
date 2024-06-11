package com.example.appagricola.principal.misCultivos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.appagricola.enviadorPeticiones.EnviadorPeticiones
import com.example.appagricola.peticionRespuesta.Peticion
import com.example.appagricola.peticionRespuesta.PeticionProblema
import com.example.appagricola.R
import com.example.appagricola.peticionRespuesta.RespuestaError
import com.example.appagricola.baseDatos.GestorDeBaseDeDatos
import com.example.appagricola.tareasActivity.TareasActivity
import com.example.appagricola.tareasActivity.calendario.CalendarioFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CultivoSeleccionadoFragment : Fragment() {
    private var idCultivo: Int = 0;
    private lateinit var cultivo: String
    private lateinit var fase: String

    companion object{
        private const val ID = "id"
        private const val CULTIVO = "cultivo"
        private const val FASE = "fase"
        fun newInstance(id: Int, cultivo:String, fase: String): CultivoSeleccionadoFragment {
            val fragment = CultivoSeleccionadoFragment()
            val args = Bundle()
            args.putInt(ID, id)
            args.putString(CULTIVO, cultivo)
            args.putString(FASE, fase)
            fragment.arguments = args
            return fragment
        }
    }
    private lateinit var botonVerCalendario: Button
    private lateinit var botonPeticion: Button
    private lateinit var botonVolver: ImageButton
    private lateinit var titulo: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var problemas: ConstraintLayout
    private lateinit var cbHumedad: CheckBox
    private lateinit var cbMoho: CheckBox
    private lateinit var cbPodrido: CheckBox
    private lateinit var cbManchas: CheckBox
    private lateinit var cbAgujHojas: CheckBox
    private lateinit var cbAgujTallo: CheckBox
    private lateinit var cbAgujFrutos: CheckBox
    private lateinit var cbInsectos: CheckBox
    private lateinit var botonEliminarCultivo: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idCultivo= it.getInt(ID)
            cultivo = it.getString(CULTIVO).toString()
            fase = it.getString(FASE).toString()
        }
    }

    override fun onStart() {
        super.onStart()
        botonVolver = activity?.findViewById(R.id.imageButtonVolverAListaCultivos)!!
        botonVolver.setOnClickListener {
            clickVolverAFragmentoCultivos()
        }
        titulo = activity?.findViewById(R.id.textViewTituloCultivo)!!
        titulo.text = cultivo.uppercase()
        radioGroup = activity?.findViewById(R.id.radioGroupTareas)!!
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            botonRadioSeleccionado(checkedId)
        }
        problemas = activity?.findViewById(R.id.constraintLayoutProblemas)!!
        radioGroup.check(R.id.radioButtonPedirTareas)

        cbHumedad = activity?.findViewById(R.id.checkBoxHumedad)!!
        cbMoho= activity?.findViewById(R.id.checkBoxMoho)!!
        cbPodrido = activity?.findViewById(R.id.checkBoxPodrido)!!
        cbManchas = activity?.findViewById(R.id.checkBoxManchas)!!

        cbAgujHojas = activity?.findViewById(R.id.checkBoxAgujerosHojas)!!
        cbAgujTallo = activity?.findViewById(R.id.checkBoxAgujerosTallo)!!
        cbAgujFrutos = activity?.findViewById(R.id.checkBoxAgujerosFrutos)!!
        cbInsectos = activity?.findViewById(R.id.checkBoxInsectosCerca)!!
        botonPeticion = activity?.findViewById(R.id.buttonPedirTareas)!!
        botonPeticion.setOnClickListener {
           lifecycleScope.launch(Dispatchers.IO) {
               clickEnviarPeticion()
           }
        }
        botonVerCalendario = activity?.findViewById(R.id.buttonIrACalendario)!!
        botonVerCalendario.setOnClickListener {
            irACalendario()
        }
        botonEliminarCultivo = activity?.findViewById(R.id.buttonQuitarCultivo)!!
        botonEliminarCultivo.setOnClickListener {
            mostrarVentanaDeConfirmarEliminacion()
        }
    }
    private fun mostrarVentanaDeConfirmarEliminacion() {
        val alerta = AlertDialog.Builder(requireContext())
        alerta.setTitle("Confirmación")
        alerta.setMessage("¿Estás seguro de que deseas eliminar este cultivo?")

        alerta.setPositiveButton("Sí") { dialog, _ ->

            dialog.dismiss()
            GestorDeBaseDeDatos.getInstance(activity).eliminarCultivo(idCultivo)
            clickVolverAFragmentoCultivos()
        }

        alerta.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = alerta.create()
        dialog.show()
    }
    private fun irACalendario(){
        val intent = Intent(context, TareasActivity::class.java)
        intent.putExtra("id",idCultivo)
        CalendarioFragment.idCultivo  = idCultivo
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cultivo_seleccionado, container, false)
    }
    private fun clickVolverAFragmentoCultivos(){
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frameContainerPrincipal, ListaCultivosFragment())
        transaction.commit()
    }
    private fun botonRadioSeleccionado(id: Int){
        if(id == R.id.radioButtonPeticionProblema){
            problemas.isVisible = true
        }
        else{
            problemas.isVisible = false
        }
    }
    private fun clickEnviarPeticion(){
        if(radioGroup.checkedRadioButtonId == R.id.radioButtonPedirTareas){
            //peticion de tareas
            enviarPeticionCultivo()
        }
        else if(radioGroup.checkedRadioButtonId == R.id.radioButtonCambiarFase){
            //peticion de nueva fase y nueva tarea
            val c = GestorDeBaseDeDatos.getInstance(activity).buscarCultivo(idCultivo)
            val faseAnterior = c.fase
            val faseNueva = c.cambiarDeFase();
            if(faseAnterior == faseNueva){
                //Aviso de que estas en la ultima fase
            }
            else{
                //Actualizar cultivo para cambiarle la fase
                GestorDeBaseDeDatos.getInstance(activity).cambiarFaseCultivo(c);
                fase = c.fase.name
                enviarPeticionCultivo()
            }
        }
        else if(radioGroup.checkedRadioButtonId == R.id.radioButtonPeticionProblema){
            //PETICION DE PROBLEMA
            enviarPeticionProblema()
        }
    }
    private fun enviarPeticionCultivo(){
        try {
            val pet : Peticion =
                Peticion()
            pet.cultivo = cultivo
            pet.fase = fase
            pet.tipo = "cultivo"
            val enviador = EnviadorPeticiones.getInstance(context)
            val respuesta = enviador.enviarPeticion(pet)
            if (respuesta.resultado == "OK"){
                GestorDeBaseDeDatos.getInstance(activity)
                    .reemplazarTareas(respuesta.calendario, idCultivo)
            }
            else{
                gestionarPeticionRechazada(respuesta.error)
            }
        }
        catch (e:Exception){
            lifecycleScope.launch(Dispatchers.Main) {
                Toast.makeText(context, "No se pudo establecer conexión con el servidor.", Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun enviarPeticionProblema(){
        try {
            val pet : Peticion =
                Peticion()
            pet.cultivo = cultivo
            pet.fase = fase
            pet.tipo = "problema"
            pet.sesion = context?.getSharedPreferences("acceso", Context.MODE_PRIVATE)
                      ?.getString("sesion","NO")
            val problema =
                PeticionProblema()
            recogerDatosHongos(problema)
            recogerDatosInsectos(problema)
            pet.problema = problema
            val enviador = EnviadorPeticiones.getInstance(context)
            val respuesta = enviador.enviarPeticion(pet)
            if (respuesta.resultado == "OK"){
                GestorDeBaseDeDatos.getInstance(activity)
                    .insertarNuevasTareas(respuesta.calendario, idCultivo)
            }
            else{
                gestionarPeticionRechazada(respuesta.error)
            }
        }
        catch (e:Exception){
            lifecycleScope.launch(Dispatchers.Main) {
                Toast.makeText(context, "No se pudo establecer conexión con el servidor.", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun gestionarPeticionRechazada(e: RespuestaError) {
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(context, e.mensaje, Toast.LENGTH_LONG).show()
        }
    }

    private fun obtenerStringAPartirDeCheckBox(checkBox: CheckBox): String{
        var respuesta = "NOK";
        if(checkBox.isChecked){
            respuesta = "OK"
        }
        return respuesta
    }
    private fun recogerDatosHongos(p: PeticionProblema): PeticionProblema {
        p.moho = obtenerStringAPartirDeCheckBox(cbMoho)
        p.humedad = obtenerStringAPartirDeCheckBox(cbHumedad)
        p.podrido = obtenerStringAPartirDeCheckBox(cbPodrido)
        p.manchasHojas = obtenerStringAPartirDeCheckBox(cbManchas)
        return p
    }

    private fun recogerDatosInsectos(p: PeticionProblema): PeticionProblema {
        p.agujerosHojas =  obtenerStringAPartirDeCheckBox(cbAgujHojas)
        p.agujerosFrutos =  obtenerStringAPartirDeCheckBox(cbAgujFrutos)
        p.agujerosTallo =  obtenerStringAPartirDeCheckBox(cbAgujTallo)
        p.insectos =  obtenerStringAPartirDeCheckBox(cbInsectos)
        return p
    }
}