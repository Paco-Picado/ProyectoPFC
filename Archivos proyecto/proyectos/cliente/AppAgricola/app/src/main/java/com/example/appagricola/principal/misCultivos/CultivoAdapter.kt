package com.example.appagricola.principal.misCultivos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appagricola.R
import com.example.appagricola.cultivos.Cultivo

class CultivoAdapter(
    private val listaCultivos: List<Cultivo>,
    private val onItemSelected: (Int) -> Unit
): RecyclerView.Adapter<CultivoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CultivoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cultivo,parent,false)
        return CultivoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaCultivos.size
    }

    override fun onBindViewHolder(holder: CultivoViewHolder, position: Int) {
        val item = listaCultivos[position]
        holder.render(item, onItemSelected)
    }
}