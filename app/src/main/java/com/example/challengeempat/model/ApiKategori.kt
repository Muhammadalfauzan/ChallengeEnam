package com.example.challengeempat.model


import com.google.gson.annotations.SerializedName

data class ApiKategori(
    @SerializedName("code")
    val codeKat: Int,
    @SerializedName("data")
    val dataKat: List<DataKategori>,
    @SerializedName("message")
    val messageKat: String?,
    @SerializedName("status")
    val statusKat: Boolean?
)