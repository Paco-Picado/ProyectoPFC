package com.example.appagricola.principal.nuevoCultivo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.appagricola.R
import com.example.appagricola.baseDatos.GestorDeBaseDeDatos
import com.example.appagricola.cultivos.Cultivo

class NuevoCultivoFragment : Fragment() {
    private lateinit var spinner: Spinner
    private lateinit var campoOtroCultivo: EditText
    private lateinit var campoAlias: EditText
    private lateinit var buttomLimpiar: Button
    private lateinit var buttonConfirmarCultivo: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nuevo_cultivo, container, false)
    }
    override fun onStart() {
        super.onStart()
        crearSpinner()
        campoOtroCultivo = activity?.findViewById(R.id.editTextOtroCultivo)!!
        campoOtroCultivo.isVisible = false;
        campoAlias = activity?.findViewById(R.id.editTextAlias)!!
        buttomLimpiar = activity?.findViewById(R.id.buttonLimpiar)!!
        buttonConfirmarCultivo = activity?.findViewById(R.id.buttonConfirmarCultivo)!!
        buttonConfirmarCultivo.setOnClickListener {
            clickConfirmarCultivo()
        }
        buttomLimpiar.setOnClickListener {
            clickLimpiar()
        }
    }
    private fun crearSpinner(){
        spinner = activity?.findViewById(R.id.spinnerCultivos)!!
        var items = listOf("Tomate Cherry","Otro")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if(selectedItem == "Otro") {
                    campoOtroCultivo.isVisible = true
                }
                else {
                    campoOtroCultivo.setText("")
                    campoOtroCultivo.isVisible = false
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    private fun clickConfirmarCultivo(){
        var nombreCultivo: String
        if(spinner.selectedItem != "Otro"){
            nombreCultivo = spinner.selectedItem.toString().lowercase()
        }
        else{
            nombreCultivo = campoOtroCultivo.text.toString().lowercase()
        }
        if(nombreCultivo.isEmpty()){
            campoOtroCultivo.error = "El campo cultivo no puede estar vacío"
        }
        else if(campoAlias.text.toString().isEmpty()){
            campoAlias.error = "El campo alias no puede estar vacío"
        }
        else{
            val gbd = GestorDeBaseDeDatos.getInstance(activity)
            if(gbd.insertarCultivo(Cultivo(nombreCultivo, campoAlias.text.toString(),Cultivo.Fase.Germinacion))){
                Toast.makeText(context,"Cultivo insertado correctamente.", Toast.LENGTH_LONG).show()
            }
            else{
                campoAlias.error = "El campo alias ya existe"
            }
        }
    }
    private fun clickLimpiar(){
        spinner.setSelection(0)
        campoAlias.setText("")
    }
}