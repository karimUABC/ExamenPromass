package com.example.examenprogramacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider

import com.example.examenprogramacion.databinding.ActivityDetallesLibroBinding

class DetallesLibro : AppCompatActivity() {
    lateinit var binding: ActivityDetallesLibroBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesLibroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Deshabilitar el título de la app en el ActionBar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Mostrar el botón de regreso en el ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tituloLibro = intent.getStringExtra("tituloLibro")
        val autor = intent.getStringExtra("autor")
        val contenido = intent.getStringExtra("contenido")
        val fecha = intent.getStringExtra("fecha")
        if (tituloLibro != null) {
            binding.tvTitu.text = tituloLibro
            binding.tvAutor.text = autor
            binding.tvConte.text = contenido
            binding.tvFecha.text = fecha
        }


    }

}