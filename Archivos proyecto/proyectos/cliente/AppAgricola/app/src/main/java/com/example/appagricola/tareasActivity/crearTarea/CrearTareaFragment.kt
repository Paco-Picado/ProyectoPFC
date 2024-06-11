package com.example.appagricola.tareasActivity.crearTarea

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.appagricola.R
import com.example.appagricola.baseDatos.GestorDeBaseDeDatos
import com.example.appagricola.cultivos.Cultivo
import com.example.appagricola.tareas.Tarea
import com.example.appagricola.tareas.TareasDia

private const val FECHA = "fecha"
private const val ID = "id"
class CrearTareaFragment : Fragment() {
    private var idCultivo: Int = 0
    private var fecha: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idCultivo = it.getInt(ID)
            fecha = it.getString(FECHA)
        }
    }
    private lateinit var botonVolverAFragmentoAnterior: ImageButton
    private lateinit var botonCrearTarea: Button
    private lateinit var nombre: EditText
    private lateinit var descripcion: EditText
    private lateinit var tvTituloFecha: TextView
    private lateinit var tvTituloCultivo: TextView
    private lateinit var tvTituloAlias: TextView
    override fun onStart() {
        super.onStart()
        botonVolverAFragmentoAnterior = activity?.findViewById(R.id.imageButtonVolverATareasDia)!!
        botonCrearTarea = activity?.findViewById(R.id.buttonCrearTarea)!!
        nombre = activity?.findViewById(R.id.editTextNombreTarea)!!
        descripcion = activity?.findViewById(R.id.editTextDescripcionTarea)!!
        botonVolverAFragmentoAnterior.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id",idCultivo)
            bundle.putString("fecha", fecha)
            findNavController().navigate(R.id.action_crearTareaFragment_to_tareasFragment, bundle)
        }
        botonCrearTarea.setOnClickListener {
            clickBotonCrearTarea()
        }
        tvTituloFecha = activity?.findViewById(R.id.textViewTituloFechaCrearTarea)!!
        tvTituloFecha.text = "Fecha: $fecha"
        tvTituloCultivo = activity?.findViewById(R.id.textViewTituloCultivoCrearTarea)!!
        val cultivo: Cultivo =  GestorDeBaseDeDatos.getInstance(activity).buscarCultivo(idCultivo);
        tvTituloCultivo.text = "Crear tarea para ${cultivo.nombre}"
        tvTituloAlias = activity?.findViewById(R.id.textViewTituloAliasCrearTarea)!!
        tvTituloAlias.text = "Alias: ${cultivo.alias}"
    }
    private fun clickBotonCrearTarea(){
        if(nombre.text.toString().isEmpty()){
            nombre.error = "El campo nombre no puede ir vacío"
        }
        else if(descripcion.text.toString().isEmpty()){
            descripcion.error = "El campo descripción no puede ir vacío"
        }
        else{
            mostrarVentanaDeConfirmarCrearTarea()
        }
    }
    private fun crearTarea(nombre: String, descripcion: String){
        val t = Tarea(nombre, descripcion);
        val dia = TareasDia(fecha);
        dia.anhadirTarea(t);
        var calendario = ArrayList<TareasDia>()
        calendario.add(dia)
        GestorDeBaseDeDatos.getInstance(activity).insertarNuevasTareas(calendario, idCultivo);
        Toast.makeText(context, "Tarea insertada correctamente", Toast.LENGTH_SHORT).show()
    }
    private fun mostrarVentanaDeConfirmarCrearTarea() {
        val alerta = AlertDialog.Builder(requireContext())
        alerta.setTitle("Confirmación")
        alerta.setMessage("¿Estás seguro de que deseas crear esta tarea?")
        alerta.setPositiveButton("Sí") { dialog, _ ->
            crearTarea(nombre.text.toString(), descripcion.text.toString())
            nombre.setText("")
            descripcion.setText("")
            dialog.dismiss()
        }
        alerta.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = alerta.create()
        dialog.show()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_tarea, container, false)
    }

    companion object {
        fun newInstance(id: Int, fecha: String) =
            CrearTareaFragment().apply {
                arguments = Bundle().apply {
                    putInt(ID, id)
                    putString(FECHA, fecha)
                }
            }
    }
}