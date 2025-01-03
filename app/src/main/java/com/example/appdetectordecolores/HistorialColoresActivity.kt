package com.example.appdetectordecolores

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class HistorialColoresActivity : AppCompatActivity() {

    private lateinit var listaColores: ListView
    private lateinit var btnRegresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_colores)

        // Referencia al ListView y al botón
        listaColores = findViewById(R.id.lista_colores)
        btnRegresar = findViewById(R.id.btn_regresar_historial)

        // Configuración del adaptador
        listaColores.adapter = AdaptadorHistorial(this, HistorialColores.obtenerHistorial())

        // Acción del botón para regresar al menú principal
        btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cierra la actividad actual para evitar que permanezca en el stack
        }
    }
}



