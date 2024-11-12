package com.eyal.exam.pelecard.modules

import com.eyal.exam.pelecard.abs.ApiService
import com.eyal.exam.pelecard.abs.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(baseUrl: String, apiKey: String): Retrofit  {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(apiKey))
            .build()
    }

    @Provides
    fun provideApiService(): ApiService {
        val retrofit = provideRetrofit(
            Constants.DEFAULT_CONVERSION_API_BASE_URL,
            Constants.DEFAULT_CONVERSION_API_KEY
        )
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideOkHttpClient(apiKey: String): OkHttpClient { // can be used for logging and other configurations
        // Create an interceptor to add the API key as a query parameter
        val apiKeyInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            // Add the API key to the URL as a query parameter
            val urlWithApiKey = originalUrl.newBuilder()
                .addQueryParameter("apikey", apiKey) // Make sure to define Constants.API_KEY
                .build()

            // Create a new request with the modified URL
            val newRequest: Request = originalRequest.newBuilder()
                .url(urlWithApiKey)
                .build()

            chain.proceed(newRequest)
        }

        // Build and return the OkHttpClient with the interceptor
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()
    }
}