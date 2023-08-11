package com.example.examenprogramacion.API

import com.example.examenprogramacion.DataClass.Libro
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ConsumirApi {
    @POST("allLibros.php")
    fun getTraer(): Call<Libro>

    @FormUrlEncoded
    @POST("addLibros.php")
    suspend fun agregarLibro(
        @Field("autor") autor: String,
        @Field("titulo") titulo: String,
        @Field("fechaPublicacion") fechaPublicacion: String,
        @Field("contenido") contenido: String
    ): Response<ResponseBody>


    @GET("searchLibrosByTitulo.php")
    fun searchLibrosPorTitulo(@Query("titulo") titulo: String): Call<Libro> // Cambiar el tipo de respuesta a Libro

    @GET("searchLibrosByAutor.php")
    fun searchLibrosPorAutor(@Query("autor") autor: String): Call<Libro> // Cambiar el tipo de respuesta a Libro

    @GET("searchLibrosByContenido.php")
    fun searchLibrosPorContenido(@Query("contenido") contenido: String): Call<Libro> // Cambiar el tipo de respuesta a Libro

}