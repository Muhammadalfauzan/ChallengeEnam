package com.example.challengeempat.model


import com.google.gson.annotations.SerializedName

data class ApiMenuResponseDua(
    @SerializedName("data")
    val datamenu: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)