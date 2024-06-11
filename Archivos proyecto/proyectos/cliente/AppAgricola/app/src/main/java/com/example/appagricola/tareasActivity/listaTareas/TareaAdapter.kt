package com.example.appagricola.tareasActivity.listaTareas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appagricola.R
import com.example.appagricola.tareas.Tarea

class TareaAdapter(
    private val listaTareas: List<Tarea>,
    private val onClickDelete: (Int) -> Unit
): RecyclerView.Adapter<TareaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea,parent,false)
        return TareaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaTareas.size
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val item = listaTareas[position]
        holder.render(item, onClickDelete)
    }
}