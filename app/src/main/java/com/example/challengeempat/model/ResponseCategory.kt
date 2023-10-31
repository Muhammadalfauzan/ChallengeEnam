package com.example.challengeempat.model


import com.google.gson.annotations.SerializedName

data class ResponseCategory(
    @SerializedName("data")
    val data : List<DataCategory>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)