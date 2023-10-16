package com.example.challengeempat.model


data class ApiMenuResponse(

    val status: Boolean,
    val code: Int,
    val message: String,
    val data: List<MenuItem>
)
