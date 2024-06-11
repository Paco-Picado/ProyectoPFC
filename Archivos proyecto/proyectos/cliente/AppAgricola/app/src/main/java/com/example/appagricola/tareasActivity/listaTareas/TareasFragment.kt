package com.example.appagricola.tareasActivity.listaTareas

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appagricola.R
import com.example.appagricola.baseDatos.GestorDeBaseDeDatos
import com.example.appagricola.tareas.Tarea
import com.example.appagricola.tareas.TareasProvider


class TareasFragment() : Fragment() {
    companion object{
        private const val ID = "id"
        private const val FECHA = "fecha"
        fun newInstance(id: Int, fecha:String): TareasFragment {
            val fragment = TareasFragment()
            val args = Bundle()
            args.putInt(ID, id)
            args.putString(FECHA, fecha)
            fragment.arguments = args
            return fragment
        }
    }
    private var idCultivo = 0
    private lateinit var fecha: String
    private lateinit var listaMutableTareas: MutableList<Tarea>
    private lateinit var adapter: TareaAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var botonIrACalendario: ImageButton
    private lateinit var botonIrAFragmentoCrearTarea: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idCultivo = it.getInt(ID)
            fecha = it.getString(FECHA).toString()
        }
    }
    override fun onStart() {
        super.onStart()
        listaMutableTareas = TareasProvider.getListaTareas(activity, idCultivo, fecha)
        Log.i("Tareas", listaMutableTareas.size.toString())
        botonIrACalendario = activity?.findViewById(R.id.imageButtonVolverACalendario)!!
        botonIrACalendario.setOnClickListener {
            irACalendario()
        }
        botonIrAFragmentoCrearTarea = activity?.findViewById(R.id.buttonIrAFragmentoCrearTarea)!!
        botonIrAFragmentoCrearTarea.setOnClickListener {
            clickBotonCrearTarea()
        }
        iniciarRecyclerViewTareas()
    }

    private fun clickBotonCrearTarea() {
        val bundle = Bundle()
        bundle.putInt("id", idCultivo)
        bundle.putString("fecha", fecha)
        findNavController().navigate(R.id.action_tareasFragment_to_crearTareaFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tareas, container, false)
    }

    private fun iniciarRecyclerViewTareas() {
        recyclerView = activity?.findViewById(R.id.rvTareas)!!
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TareaAdapter(
            listaTareas = listaMutableTareas,
            onClickDelete = { position -> onDeletedItem(position) }
        )
        recyclerView.adapter = adapter
    }

    private fun onDeletedItem(position: Int) {
        val t = listaMutableTareas[position]
        GestorDeBaseDeDatos.getInstance(activity).eliminarTarea(t.id)
        listaMutableTareas.removeAt(position)
        adapter.notifyItemRemoved(position)
    }
    private fun irACalendario(){
        val bundle = Bundle()
        bundle.putInt("id", idCultivo)
        bundle.putString("fecha", fecha)
        findNavController().navigate(R.id.action_tareasFragment_to_calendarioFragment, bundle)
    }
}
