package com.capstone.cendekiaone.data.remote.retforit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        // Function to create and configure the ApiService instance
        fun getApiService(): ApiService {
            // Create an interceptor for logging network requests and responses
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            // Create an OkHttpClient with the logging interceptor
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            // Create a Retrofit instance with the base URL, Gson converter, and OkHttpClient
            val retrofit = Retrofit.Builder()
                .baseUrl("https://337d-2405-8180-801-e4fc-cad-6abd-8ae5-83f9.ngrok-free.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            // Create and return an ApiService implementation
            return retrofit.create(ApiService::class.java)
        }
    }
}