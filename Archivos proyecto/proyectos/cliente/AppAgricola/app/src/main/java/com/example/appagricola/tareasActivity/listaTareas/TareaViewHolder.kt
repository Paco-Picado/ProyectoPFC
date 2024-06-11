package com.example.appagricola.tareasActivity.listaTareas

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.appagricola.R
import com.example.appagricola.tareas.Tarea

class TareaViewHolder(view: View):ViewHolder(view) {
    private val nombre = view.findViewById<TextView>(R.id.tvNombreTarea)
    private val descripcion = view.findViewById<TextView>(R.id.tvDescripcionTarea)
    private val checkBox = view.findViewById<CheckBox>(R.id.checkBoxTarea)
    fun render(t: Tarea, onClickDelete: (Int) -> Unit) {
        nombre.text = t.nombre.uppercase()
        descripcion.text = t.descripcion.uppercase()
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                onClickDelete(adapterPosition)
            }
        }
    }
}