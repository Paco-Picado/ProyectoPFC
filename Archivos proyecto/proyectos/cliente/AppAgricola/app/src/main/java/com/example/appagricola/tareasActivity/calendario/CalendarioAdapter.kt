package com.example.appagricola.tareasActivity.calendario

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appagricola.R

class CalendarioAdapter(
    private val listaDias: List<Dia>,
    private val onClickListener: (Int) -> Unit
): RecyclerView.Adapter<CalendarioViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celda_calendario,parent,false)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarioViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaDias.size
    }

    override fun onBindViewHolder(holder: CalendarioViewHolder, position: Int) {
        val item = listaDias[position]
        holder.render(item, onClickListener)
    }
}