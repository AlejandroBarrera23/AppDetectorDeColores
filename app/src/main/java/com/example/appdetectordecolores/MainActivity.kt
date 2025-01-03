package com.example.appdetectordecolores

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDetectarColor = findViewById<Button>(R.id.btn_detectar_color)
        val btnHistorialColores = findViewById<Button>(R.id.btn_historial_colores)

        btnDetectarColor.setOnClickListener {
            startActivity(Intent(this, ColorDetectionActivity::class.java))
        }

        btnHistorialColores.setOnClickListener {
            startActivity(Intent(this, HistorialColoresActivity::class.java))
        }
    }
}

