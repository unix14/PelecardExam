package com.eyal.exam.pelecard.di

import android.util.Log
import com.eyal.exam.pelecard.abs.Constants
import com.eyal.exam.pelecard.network.ApiService
import com.eyal.exam.pelecard.network.PaymentService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(baseUrl: String, apiKey: String? = null): Retrofit  {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(apiKey))
            .build()
    }

    @Provides
    fun provideApiService(): ApiService = provideRetrofit(
        Constants.DEFAULT_CONVERSION_API_BASE_URL,
        Constants.DEFAULT_CONVERSION_API_KEY
    ).create(ApiService::class.java)

    @Provides
    fun providePaymentService(): PaymentService = provideRetrofit(Constants.DEFAULT_PAYMENT_API_BASE_URL)
        .create(PaymentService::class.java)

    @Provides
    fun provideOkHttpClient(apiKey: String? = null): OkHttpClient { // can be used for logging and other configurations
        // Create an interceptor to add the API key as a query parameter
        val apiKeyInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            var urlBuilder = originalUrl.newBuilder()
            if(apiKey != null) {
                // Add the API key to the URL as a query parameter
                urlBuilder = urlBuilder.addQueryParameter("apikey", apiKey)
            }
            val urlWithApiKey = urlBuilder.build()

            // Create a new request with the modified URL
            val newRequest: Request = originalRequest.newBuilder()
                .url(urlWithApiKey)
                .build()

            Log.d("NetworkModule", "provideOkHttpClient: $urlWithApiKey")
            chain.proceed(newRequest)
        }
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        // Build and return the OkHttpClient with the interceptor
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(logging)
            .build()
    }
}