package com.example.examenprogramacion

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.examenprogramacion.API.ConsumirApi
import com.example.examenprogramacion.API.RetrofitClient
import com.example.examenprogramacion.DataClass.Item
import com.example.examenprogramacion.DataClass.Libro

import com.example.examenprogramacion.databinding.ActivityMenuBinding
import com.example.examenprogramacion.offlineDatabase.Companion.COLUMN_AUTOR
import com.example.examenprogramacion.offlineDatabase.Companion.COLUMN_CONTENIDO
import com.example.examenprogramacion.offlineDatabase.Companion.COLUMN_FECHA
import com.example.examenprogramacion.offlineDatabase.Companion.COLUMN_TITULO
import com.example.examenprogramacion.offlineDatabase.Companion.TABLE_NAME
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Menu : AppCompatActivity() {
    lateinit var binding: ActivityMenuBinding
    private lateinit var retrofitService: ConsumirApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofitService = RetrofitClient.consumirApi

        val database = offlineDatabase(this)
        val call = retrofitService.getTraer()

        if (isNetworkAvailable()) {

            binding.cardAdd.setOnClickListener {
                val intent = Intent(applicationContext, AltaLibro::class.java)
                startActivity(intent)
                //finish()
            }

            binding.cardDownload.setOnClickListener {
                call.enqueue(object : Callback<Libro> {
                    override fun onResponse(call: Call<Libro>, response: Response<Libro>) {
                        if (response.isSuccessful) {

                            val libros = response.body()?.items

                            if (libros?.isNotEmpty() == true) {

                                val db = database.writableDatabase

                                db.beginTransaction()
                                try {
                                    for (libro in libros) {
                                        if (!isBookAlreadyInDatabase(db, libro)) {
                                            val values = ContentValues().apply {
                                                put(COLUMN_AUTOR, libro.autor)
                                                put(COLUMN_TITULO, libro.titulo)
                                                put(COLUMN_FECHA, libro.fechaPublicacion)
                                                put(COLUMN_CONTENIDO, libro.contenido)
                                            }
                                            db.insert(TABLE_NAME, null, values)
                                        }

                                    }
                                    db.setTransactionSuccessful()
                                    Toast.makeText(this@Menu, "Se descargaron los datos correctamente.", Toast.LENGTH_SHORT).show()
                                } finally {
                                    db.endTransaction()
                                }

                            } else {
                                Toast.makeText(this@Menu, "No se encontraron libros.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Manejar errores de respuesta
                        }
                    }

                    override fun onFailure(call: Call<Libro>, t: Throwable) {

                        Toast.makeText(this@Menu, "Error al consultar la API: ${t.message}", Toast.LENGTH_SHORT).show()

                        Log.e("API Error", "Error al consultar la API: ${t.message}")
                    }
                })
            }
        } else {
            Toast.makeText(this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show()
            binding.cardAdd.isEnabled=false
            binding.cardDownload.isEnabled=false
        }

        binding.cardSearch.setOnClickListener {
            val intent = Intent(applicationContext, MenuSearch::class.java)
            startActivity(intent)
            //finish()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun isBookAlreadyInDatabase(db: SQLiteDatabase, libro: Item): Boolean {
        val selection = "$COLUMN_TITULO = ? OR $COLUMN_AUTOR = ?"
        val selectionArgs = arrayOf(libro.titulo, libro.autor)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
        val bookExists = cursor.count > 0
        cursor.close()
        return bookExists
    }

}