package com.example.challengeempat.model


import com.google.gson.annotations.SerializedName

data class DataKategori(
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("nama")
    val namaKat: String
)