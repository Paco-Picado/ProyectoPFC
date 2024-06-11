package com.example.appagricola.principal.misCultivos

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.appagricola.R
import com.example.appagricola.cultivos.Cultivo

class CultivoViewHolder(view: View):ViewHolder(view) {
    private var idCultivo: Int = 0
    private val nombre = view.findViewById<TextView>(R.id.tvCultivo)
    private val alias = view.findViewById<TextView>(R.id.tvAlias)
    private val fase = view.findViewById<TextView>(R.id.tvFase)
    private val itemCultivo: View = view
    fun render(c: Cultivo, onItemSelected: (Int) -> Unit) {
        idCultivo = c.id
        nombre.text = c.nombre.uppercase()
        alias.text = c.alias.uppercase()
        fase.text = c.fase.name.uppercase()
        itemCultivo.setOnClickListener {
            onItemSelected(adapterPosition)
        }
    }
}