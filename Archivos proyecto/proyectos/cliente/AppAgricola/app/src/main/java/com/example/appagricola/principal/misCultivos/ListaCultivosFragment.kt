package com.example.appagricola.principal.misCultivos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appagricola.cultivos.Cultivo
import com.example.appagricola.cultivos.CultivosProvider
import androidx.fragment.app.FragmentTransaction
import com.example.appagricola.R

class ListaCultivosFragment : Fragment() {

    private lateinit var listaCultivos: ArrayList<Cultivo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
        val recycler = activity?.findViewById<RecyclerView>(R.id.rvCultivos)!!
        recycler.layoutManager = LinearLayoutManager(context)
        listaCultivos = CultivosProvider.getListaCultivos(activity)
        recycler.adapter = CultivoAdapter(
            listaCultivos,
            onItemSelected = {position-> onItemSelected(position)}
            )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_cultivos, container, false)
    }
    //Funcion cuando se selecciona un vultivo
    private fun onItemSelected(position: Int){
        val cultivo = listaCultivos[position]
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        val fragmento = CultivoSeleccionadoFragment()
        val bundle = Bundle()
        bundle.putInt("id", cultivo.id)
        bundle.putString("cultivo", cultivo.nombre)
        bundle.putString("fase", cultivo.fase.name)
        fragmento.arguments = bundle
        transaction.replace(R.id.frameContainerPrincipal, fragmento)
        //Ir a fragmento cultivo Seleccionado pasando argumentos bundle
        transaction.commit()
    }
}