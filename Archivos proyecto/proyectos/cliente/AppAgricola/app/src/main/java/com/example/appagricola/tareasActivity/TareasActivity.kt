package com.example.appagricola.tareasActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.appagricola.R

class TareasActivity() : AppCompatActivity() {
    companion object{
        const val ID ="id"
    }
    private var idCultivo: Int = 0;
    private lateinit var navegador: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tareas)
        idCultivo = intent.getIntExtra(ID,0)
        Log.i("ID",idCultivo.toString())

    }
    override fun onStart() {
        super.onStart()
        navegador = Navigation.findNavController(findViewById(R.id.fragmentContainerViewTareas))
    }
}