package com.example.challengeempat.api



import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Api {

    private const val BASE_URL = "https://39cc9dac-21fc-401d-9cec-9100ffc66406.mock.pstmn.io/"

    val apiService: ApiRestaurant by lazy {
        createApiService()
    }

    private fun createApiService(): ApiRestaurant {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiRestaurant::class.java)
    }


}
