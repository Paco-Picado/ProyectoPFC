package com.example.appagricola.principal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.appagricola.R
import com.example.appagricola.principal.misCultivos.ListaCultivosFragment
import com.example.appagricola.principal.nuevoCultivo.NuevoCultivoFragment
import com.example.appagricola.principal.sesiones.ConfiguracionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class PrincipalActivity : AppCompatActivity() {
    private lateinit var fragmentoMisCultivos: Fragment
    private lateinit var fragmentoNuevoCultivo: Fragment
    private lateinit var fragmentoConfiguracion: Fragment
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
    }

    override fun onStart() {
        super.onStart()
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnItemSelectedListener { item  ->
            cambiarFragmento(item)
        }
        fragmentoMisCultivos = ListaCultivosFragment()
        cargarFragmento(fragmentoMisCultivos)
    }
    private fun cambiarFragmento(item:MenuItem): Boolean {
        var cambio = true;
        if(item.itemId== R.id.fragmentoMisCultivos){
            fragmentoMisCultivos = ListaCultivosFragment()
            cargarFragmento(fragmentoMisCultivos)
        }
        else if(item.itemId == R.id.fragmentoNuevoCultivo){
            fragmentoNuevoCultivo = NuevoCultivoFragment()
            cargarFragmento(fragmentoNuevoCultivo)
        }
        else if(item.itemId == R.id.fragmentoConfiguracion){
            fragmentoConfiguracion = ConfiguracionFragment()
            cargarFragmento(fragmentoConfiguracion)
        }
        else{
            cambio = false
        }
        return cambio
    }
    private fun cargarFragmento(f:Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameContainerPrincipal, f)
        transaction.commit()
    }
}