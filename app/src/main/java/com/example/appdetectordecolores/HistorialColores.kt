package com.example.appdetectordecolores

object HistorialColores {
    private val historial = mutableListOf<Int>()

    fun agregarColor(color: Int) {
        historial.add(color)
    }

    fun obtenerHistorial(): List<Int> {
        return historial
    }
}
