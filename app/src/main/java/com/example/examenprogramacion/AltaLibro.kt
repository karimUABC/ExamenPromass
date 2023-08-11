package com.example.examenprogramacion

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.examenprogramacion.API.ConsumirApi
import com.example.examenprogramacion.API.RetrofitClient
import com.example.examenprogramacion.databinding.ActivityAltaLibroBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AltaLibro : AppCompatActivity() {
    lateinit var binding: ActivityAltaLibroBinding
    private val retrofit = RetrofitClient.retrofit
    private val consumirApi = retrofit.create(ConsumirApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAltaLibroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Mostrar el botón de regreso en el ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (isNetworkAvailable()) {
            binding.btnIngresar.setOnClickListener {

                val currentDate = getCurrentDate()
                val autor = binding.EtAutor.text.toString()
                val titulo = binding.EtTitulo.text.toString()
                val contenido = binding.EtConte.text.toString()


                if (currentDate.isNotEmpty()
                    && autor.isNotEmpty()
                    && titulo.isNotEmpty()
                    && contenido.isNotEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val response = consumirApi.agregarLibro(autor,titulo,currentDate,contenido)

                            if (response.isSuccessful) {

                                val responseBody = response.body()

                                if (responseBody != null) {

                                    Toast.makeText(this@AltaLibro, "Se registro correctamente con la fecha: $currentDate", Toast.LENGTH_SHORT).show()

                                } else {

                                    Toast.makeText(this@AltaLibro, "Ocurrio un error, la respuesta fue nula", Toast.LENGTH_SHORT).show()

                                }

                            } else {
                                Toast.makeText(this@AltaLibro, "Ocurrio un error, la respuesta no fue exitosa", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this@AltaLibro, "Ocurrio un error, $e", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Llena todos los datos", Toast.LENGTH_SHORT).show()
                }


            }
        }else{
            Toast.makeText(this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show()
            binding.btnIngresar.isEnabled=false
        }


        binding.EtConte.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se utiliza en este caso
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se utiliza en este caso
            }

            override fun afterTextChanged(s: Editable?) {
                val maxLength = 100 // Límite máximo de caracteres
                if ((s?.length ?: 0) > maxLength) {
                    binding.EtConte.text?.delete(maxLength, binding.EtConte.text?.length ?: 0)
                }
            }
        })


    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // El mes comienza desde 0
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val formattedMonth = String.format("%02d", month)

        return "$year-$formattedMonth-$day"
    }
}