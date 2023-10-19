package com.example.challengeempat.api



import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Api {

    private const val BASE_URL = "https://236c3839-2d73-48b4-9e05-1e7ee3873548.mock.pstmn.io/"
 /*   private const val BASE_URL = "https://testing.jasa-nikah-siri-amanah-profesional.com/"*/

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
