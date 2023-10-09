package com.example.challengeempat.api

import com.example.challengeempat.model.ApiKategori
import com.example.challengeempat.model.ApiMenuResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiRestaurant {

    @GET("/listmenu")
   fun getAllMenu(): Call<ApiMenuResponse>

   @GET("/category")
   fun getKategori(): Call<ApiKategori>

}
