package com.example.challengeempat.modelapi


import com.google.gson.annotations.SerializedName

data class ResponseCategory(
    @SerializedName("data")
    val `data`: List<DataCategory>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)