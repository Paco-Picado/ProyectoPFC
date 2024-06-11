package com.example.appagricola.tareasActivity.calendario

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.appagricola.R

class CalendarioViewHolder(view: View): ViewHolder(view) {
    private val diaDelMes: TextView = view.findViewById(R.id.tvDia)
    private val colorFondo = ContextCompat.getColor(view.context, R.color.azul_claro)
    fun render(d: Dia, onClickListener: (Int) -> Unit) {
        diaDelMes.text = d.fecha
        if(d.isHayTareas){
            //Si hay tareas se pinta de azul el dia del calendario
            diaDelMes.setBackgroundColor(colorFondo)
        }
        if (onClickListener != null && d.fecha.isNotEmpty()){
            //Evento de click si onCLickListener != null y hay fecha
            diaDelMes.setOnClickListener{onClickListener(adapterPosition)}
        }
    }
}