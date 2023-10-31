package com.example.challengeempat.api



/*object Api {

    *//*private const val BASE_URL = "https://236c3839-2d73-48b4-9e05-1e7ee3873548.mock.pstmn.io/"*//*
    const val BASE_URL = "https://testing.jasa-nikah-siri-amanah-profesional.com/"

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


}*/
