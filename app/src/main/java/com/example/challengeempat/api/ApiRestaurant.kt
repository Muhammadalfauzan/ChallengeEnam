package com.example.challengeempat.api

import com.example.challengeempat.model.ApiMenuResponseDua
import com.example.challengeempat.model.ApiOrderRequest
import com.example.challengeempat.model.OrderResponse
import com.example.challengeempat.model.ResponseCategory
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiRestaurant {

    @GET("listmenu")
    suspend fun getAllMenu(): Response<ApiMenuResponseDua>

    @GET("category-menu")
    suspend fun getAllCategory(): Response<ResponseCategory>

    @POST("order-menu")
    fun order(
        @Body orderRequest: ApiOrderRequest
    ): Call<OrderResponse>
}


