package com.example.challengeempat.di

import android.content.Context
import com.example.challengeempat.api.ApiRestaurant
import com.example.challengeempat.database.CartDao
import com.example.challengeempat.database.DatabaseCart
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {
    @Provides
    @Singleton
    fun provideDatabaseCart(@ApplicationContext context: Context): DatabaseCart {
        return DatabaseCart.getInstance(context)
    }
    @Singleton
    @dagger.Provides
    fun getDataDao(kelolaDB: DatabaseCart): CartDao =
        kelolaDB.cartDao()
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    fun provideMasterService(okHttpClient: OkHttpClient): ApiRestaurant =
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiRestaurant::class.java)
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiRestaurant {
        return retrofit.create(ApiRestaurant::class.java)
    }
}
