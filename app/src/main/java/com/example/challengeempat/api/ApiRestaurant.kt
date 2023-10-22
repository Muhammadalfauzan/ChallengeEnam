package com.example.challengeempat.api

import com.example.challengeempat.model.ApiOrderRequest
import com.example.challengeempat.model.OrderResponse
import com.example.challengeempat.modelapi.ApiMenuResponseDua
import com.example.challengeempat.modelapi.ResponseCategory
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiRestaurant {

/*    @GET("listmenu")
    suspend fun getAllMenu(): Response<ApiMenuResponse>*/
    @GET("listmenu")
    suspend fun getAllMenu(): Response<ApiMenuResponseDua>
   /*@GET("/category")
   fun getKategori(): Call<ApiKategori>*/
    @GET("category-menu")
    fun getCategory(): Call<ResponseCategory>

    /*@POST("order")
    fun order(
        @Body orderRequest: ApiOrderRequest
    ): Call<OrderResponse>*/
    @POST("order-menu")
    fun order(
        @Body orderRequest: ApiOrderRequest
    ): Call<OrderResponse>
}


