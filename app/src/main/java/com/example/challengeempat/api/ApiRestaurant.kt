package com.example.challengeempat.api

import com.example.challengeempat.model.ApiKategori
import com.example.challengeempat.model.ApiMenuResponse
import com.example.challengeempat.model.ApiOrderRequest
import com.example.challengeempat.model.OrderResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiRestaurant {

    @GET("listmenu")
    suspend fun getAllMenu(): Response<ApiMenuResponse>



   @GET("/category")
   fun getKategori(): Call<ApiKategori>

    @POST("order")
    fun order(
        @Body orderRequest: ApiOrderRequest
    ): Call<OrderResponse>
}

/*    @GET("listmenu")
    suspend fun getAllMenu(): Response<ApiMenuResponseDua>*/
