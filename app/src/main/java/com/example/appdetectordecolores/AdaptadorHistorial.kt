package com.example.appdetectordecolores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AdaptadorHistorial(private val context: Context, private val colores: List<Int>) : BaseAdapter() {

    override fun getCount(): Int = colores.size
    override fun getItem(position: Int): Int = colores[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_color_historial, parent, false)

        val previsualizacion = view.findViewById<View>(R.id.previsualizacion_color_historial)
        val codigoColor = view.findViewById<TextView>(R.id.codigo_color_historial)

        val color = colores[position]
        previsualizacion.setBackgroundColor(color)
        codigoColor.text = String.format("#%06X", 0xFFFFFF and color)

        return view
    }
}

