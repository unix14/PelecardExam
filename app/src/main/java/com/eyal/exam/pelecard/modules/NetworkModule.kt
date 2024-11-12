package com.eyal.exam.pelecard.modules

import com.eyal.exam.pelecard.abs.ApiService
import com.eyal.exam.pelecard.abs.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(baseUrl: String): Retrofit  {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
    }

    @Provides
    fun provideApiService(): ApiService {
        val retrofit = provideRetrofit(Constants.DEFAULT_CONVERSION_API_BASE_URL)
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient { // can be used for logging and other configurations
        return OkHttpClient.Builder().build()
    }
}