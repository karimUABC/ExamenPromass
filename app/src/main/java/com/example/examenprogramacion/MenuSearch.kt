package com.example.examenprogramacion

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examenprogramacion.API.ConsumirApi
import com.example.examenprogramacion.API.RetrofitClient
import com.example.examenprogramacion.DataClass.Item
import com.example.examenprogramacion.DataClass.Libro
import com.example.examenprogramacion.databinding.ActivityMenuSearchBinding
import com.example.examenprogramacion.offlineDatabase.Companion.COLUMN_AUTOR
import com.example.examenprogramacion.offlineDatabase.Companion.COLUMN_CONTENIDO
import com.example.examenprogramacion.offlineDatabase.Companion.COLUMN_FECHA
import com.example.examenprogramacion.offlineDatabase.Companion.COLUMN_TITULO
import com.example.examenprogramacion.offlineDatabase.Companion.TABLE_NAME
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuSearch : AppCompatActivity() {

    lateinit var binding: ActivityMenuSearchBinding
    private lateinit var retrofitService: ConsumirApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Deshabilitar el título de la app en el ActionBar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Mostrar el botón de regreso en el ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        retrofitService = RetrofitClient.consumirApi

        val libroAdapter = LibroAdapter(emptyList())
        binding.recyclerView.adapter = libroAdapter


        binding.cardContenido.setOnClickListener {
            val intent = Intent(applicationContext, BusquedaPorContenido::class.java)
            startActivity(intent)
        }

        binding.cardAutor.setOnClickListener {
            val intent = Intent(applicationContext, BusquedaPorAutor::class.java)
            startActivity(intent)
        }

        binding.cardTitulo.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val call = retrofitService.getTraer()

        if (isNetworkAvailable()) {
            call.enqueue(object : Callback<Libro> {
                override fun onResponse(call: Call<Libro>, response: Response<Libro>) {
                    if (response.isSuccessful) {

                        val libros = response.body()

                        if (libros?.items?.isNotEmpty() == true) {

                            libroAdapter.updateData(libros.items)

                        } else {

                            libroAdapter.clearData()
                            Toast.makeText(this@MenuSearch, "No se encontraron libros.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Manejar errores de respuesta
                    }
                }

                override fun onFailure(call: Call<Libro>, t: Throwable) {
                    libroAdapter.clearData()
                    Toast.makeText(this@MenuSearch, "Error al consultar la API: ${t.message}", Toast.LENGTH_SHORT).show()

                    Log.e("API Error", "Error al consultar la API: ${t.message}")
                }
            })
        }else{

            binding.cardTitulo.isEnabled=false
            binding.cardAutor.isEnabled=false
            binding.cardContenido.isEnabled=false

            val database = offlineDatabase(this@MenuSearch).readableDatabase
            val columns = arrayOf(COLUMN_AUTOR, COLUMN_TITULO, COLUMN_FECHA, COLUMN_CONTENIDO)
            val orderBy = "$COLUMN_TITULO DESC"
            val cursor = database.query(TABLE_NAME, columns, null, null, null, null, orderBy)

            val libros = mutableListOf<Item>()

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val autor = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTOR))
                    val titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO))
                    val fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA))
                    val contenido = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENIDO))

                    val libro = Item(autor, contenido, fecha, null, titulo)
                    libros.add(libro)
                } while (cursor.moveToNext())

                cursor.close()
                libroAdapter.updateData(libros)
                Toast.makeText(this@MenuSearch, "Datos del servidor local.", Toast.LENGTH_SHORT).show()
            } else {
                libroAdapter.clearData()
                Toast.makeText(this@MenuSearch, "No se encontraron libros en la base de datos local.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}