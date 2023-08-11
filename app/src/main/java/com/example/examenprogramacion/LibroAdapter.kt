package com.example.examenprogramacion


import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.examenprogramacion.DataClass.Item


class LibroAdapter(private var libros: List<Item>) :
    RecyclerView.Adapter<LibroAdapter.LibroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.itemlibro, parent, false)
        return LibroViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val libro = libros[position]
        holder.bind(libro)
    }

    fun clearData() {
        libros = emptyList()
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return libros.size
    }

    fun updateData(newLibros: List<Item>) {
        libros = newLibros
        notifyDataSetChanged()
    }

    inner class LibroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(libro: Item) {
            val tvAutor = itemView.findViewById<TextView>(R.id.tvAutor)
            val tvTitulo = itemView.findViewById<TextView>(R.id.tvTitulo)
            val tvContenido = itemView.findViewById<TextView>(R.id.tvContenido)
            val tvFecha = itemView.findViewById<TextView>(R.id.tvFecha)

            // Limitar el contenido a 70 caracteres
            val contenido = libro.contenido
            val contenidoCorto = if (contenido.length > 70) {
                contenido.substring(0, 70) + "..."
            } else {
                contenido
            }

            tvAutor.text = libro.autor
            tvTitulo.text = libro.titulo
            tvContenido.text=contenidoCorto
            tvFecha.text=libro.fechaPublicacion

            itemView.setOnClickListener {
                val context = it.context
                val intent = Intent(context, DetallesLibro::class.java)
                intent.putExtra("tituloLibro", libro.titulo)
                intent.putExtra("autor", libro.autor)
                intent.putExtra("contenido", libro.contenido)
                intent.putExtra("fecha", libro.fechaPublicacion)
                context.startActivity(intent)
            }



        }
    }
}