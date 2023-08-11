package com.example.examenprogramacion.DataClass

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("autor")
    val autor: String,
    @SerializedName("contenido")
    val contenido: String,
    @SerializedName("fechaPublicacion")
    val fechaPublicacion: String,
    @SerializedName("libroId")
    val libroId: Int?,
    @SerializedName("titulo")
    val titulo: String

)