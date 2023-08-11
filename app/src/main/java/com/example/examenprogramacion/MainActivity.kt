package com.example.examenprogramacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examenprogramacion.DataClass.Libro
import com.example.examenprogramacion.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.Menu

import com.example.examenprogramacion.API.ConsumirApi
import com.example.examenprogramacion.API.RetrofitClient

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var retrofitService: ConsumirApi
    private lateinit var libroAdapter: LibroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Deshabilitar el título de la app en el ActionBar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Mostrar el botón de regreso en el ActionBar (opcional)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        retrofitService = RetrofitClient.consumirApi

        libroAdapter = LibroAdapter(emptyList())
        binding.recyclerView.adapter = libroAdapter


        binding.recyclerView.layoutManager = LinearLayoutManager(this)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Buscar libros por título"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                buscarLibrosPorTitulo(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                buscarLibrosPorTitulo(newText ?: "")
                return true
            }
        })

        return true
    }

    private fun buscarLibrosPorTitulo(titulo: String) {
        if (titulo.isNotEmpty()) {
            val call = retrofitService.searchLibrosPorTitulo(titulo)
            call.enqueue(object : Callback<Libro> {
                override fun onResponse(call: Call<Libro>, response: Response<Libro>) {
                    if (response.isSuccessful) {
                        val libros = response.body()
                        if (libros?.items?.isNotEmpty() == true) {
                            libroAdapter.updateData(libros.items)
                        } else {
                            libroAdapter.clearData()
                            Toast.makeText(this@MainActivity, "No se encontraron libros con ese título.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Manejar errores de respuesta
                    }
                }

                override fun onFailure(call: Call<Libro>, t: Throwable) {
                    libroAdapter.clearData()
                    Toast.makeText(this@MainActivity, "Error al consultar la API: ${t.message}", Toast.LENGTH_SHORT).show()

                    Log.e("API Error", "Error al consultar la API: ${t.message}")
                }
            })
        } else {
            libroAdapter.clearData()
        }
    }

}