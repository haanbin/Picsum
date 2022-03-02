package com.example.picsum.di

import com.example.picsum.BuildConfig
import com.example.picsum.data.remote.service.PicsumService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    private const val CONTENT_TYPE = "Content-Type"
    private const val APPLICATION_JSON = "application/json"

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideInterceptor())
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return interceptor
    }

    @Singleton
    @Provides
    fun providePicsumService(): PicsumService = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_URL)
        .addConverterFactory(provideGsonConverterFactory())
        .client(provideOkHttpClient())
        .build()
        .create(PicsumService::class.java)
}
