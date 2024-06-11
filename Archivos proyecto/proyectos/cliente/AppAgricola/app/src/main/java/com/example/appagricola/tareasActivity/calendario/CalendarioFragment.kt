package com.example.appagricola.tareasActivity.calendario

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appagricola.principal.PrincipalActivity
import com.example.appagricola.R
import com.example.appagricola.baseDatos.GestorDeBaseDeDatos
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarioFragment : Fragment() {
    companion object{
        var idCultivo = 0;
        private const val ID = "id"
        fun newInstance(id: Int): CalendarioFragment {
            val fragment = CalendarioFragment()
            val args = Bundle()
            args.putInt(ID, id)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var tvMesAnho: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fechaSeleccionada: LocalDate
    private lateinit var siguienteMes: Button
    private lateinit var anteriorMes: Button
    private lateinit var listaDiasMesActual: ArrayList<Dia>
    private lateinit var mesAnhoActual: String
    private lateinit var ibtVolverActivityPrincipal: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idCultivo = it.getInt(ID)
        }
    }
    override fun onStart() {
        super.onStart()
        tvMesAnho = activity?.findViewById(R.id.tvMesAnho)!!
        recyclerView = activity?.findViewById(R.id.rvCalendario)!!
        fechaSeleccionada = LocalDate.now()
        setVistaMes()
        siguienteMes = activity?.findViewById(R.id.btMesSiguiente)!!
        anteriorMes = activity?.findViewById(R.id.btMesAnterior)!!
        siguienteMes.setOnClickListener {
            siguienteMes()
        }
        anteriorMes.setOnClickListener {
            anteriorMes()
        }
        ibtVolverActivityPrincipal = activity?.findViewById(R.id.ibtVolverActivityPrincipal)!!
        ibtVolverActivityPrincipal.setOnClickListener {
            clickVolverActivityPrincipal();
        }
    }

    private fun clickVolverActivityPrincipal() {
        val intent = Intent(context, PrincipalActivity::class.java)
        intent.putExtra("id", idCultivo)
        startActivity(intent)
    }

    private fun setVistaMes(){
        tvMesAnho.text = obtenerMesDeFecha(fechaSeleccionada)
        listaDiasMesActual = obtenerDiasDelMes(fechaSeleccionada)
        val calendarioAdapter = CalendarioAdapter(
            listaDias = listaDiasMesActual,
            onClickListener = {position-> onItemSelected(position)}
        )
        recyclerView.layoutManager = GridLayoutManager(context, 7)
        recyclerView.adapter = calendarioAdapter
    }
    private fun siguienteMes(){
        fechaSeleccionada = fechaSeleccionada.plusMonths(1)
        setVistaMes()
    }
    private fun anteriorMes(){
        fechaSeleccionada = fechaSeleccionada.minusMonths(1)
        setVistaMes()
    }
    private fun obtenerDiasDelMes(d: LocalDate): ArrayList<Dia>{
        val lista: ArrayList<Dia> = ArrayList()
        val anhoMes: YearMonth = YearMonth.from(d)
        val diasMes = anhoMes.lengthOfMonth()
        val primeroDeMes = d.withDayOfMonth(1)
        val diaDeLaSemana = primeroDeMes.dayOfWeek.value
        // Agregar espacios en blanco antes del primer día del mes
        for (i in 1 until diaDeLaSemana) {
            lista.add(Dia(""))
        }
        // Agregar los días del mes
        for (dia in 1..diasMes) {
            lista.add(Dia(dia.toString()))
        }
        //Se marcan los dias que tienen tareas, el mes es formateado para que tenga dos digitos
        marcarDiasConTareas(lista, anhoMes.monthValue.toString().padStart(2, '0'), anhoMes.year.toString())
        return lista
    }
    private fun marcarDiasConTareas(lista: ArrayList<Dia>, mes: String, anho: String): ArrayList<Dia>{
        mesAnhoActual = "$mes/$anho"
        val diasConTareas: ArrayList<String> =
            GestorDeBaseDeDatos.getInstance(activity).buscarTareasDelMes(idCultivo, "%/$mesAnhoActual")
        for(d in lista){
            //Formatear el dia para que haya siempre dos numeros (ej: 08)
            val diaFormateado = d.fecha.toString().padStart(2, '0')
            d.isHayTareas = diasConTareas.contains(diaFormateado)
        }
        return lista
    }
    private fun obtenerMesDeFecha(d: LocalDate): String{
        val formato: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return d.format(formato)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendario, container, false)
    }
    private fun onItemSelected(position: Int){
        val diaFormateado = listaDiasMesActual[position].fecha.toString().padStart(2, '0')
        abrirTareasDia("$diaFormateado/$mesAnhoActual")
    }
    private fun abrirTareasDia(fecha: String){
        //Llamar al fragmento tareas pasandole argumentos bundle
        val bundle = Bundle().apply {
            putInt("id", idCultivo)
            putString("fecha", fecha)
        }
        findNavController().navigate(R.id.action_calendarioFragment_to_tareasFragment, bundle)
    }
}