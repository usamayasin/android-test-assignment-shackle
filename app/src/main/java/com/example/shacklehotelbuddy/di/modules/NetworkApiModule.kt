package com.example.shacklehotelbuddy.di.modules

import com.example.shacklehotelbuddy.BuildConfig
import com.example.shacklehotelbuddy.data.remote.ApiService
import com.example.shacklehotelbuddy.data.remote.ConnectivityInterceptor
import com.example.shacklehotelbuddy.utils.Constants
import com.example.shacklehotelbuddy.utils.Constants.HEADER_API_HOST
import com.example.shacklehotelbuddy.utils.Constants.HEADER_API_KEY
import com.google.gson.Gson
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

/**
 * The Dagger Module to provide the instances of [OkHttpClient], [Retrofit], and [ApiService] classes.
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkApiModule {

    @Singleton
    @Provides
    fun provideOKHttpClient(): OkHttpClient {
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val apiInterceptor = Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader(
                        HEADER_API_KEY, BuildConfig.API_KEY
                    ).addHeader(
                        HEADER_API_HOST, BuildConfig.API_HOST
                    )
                    .build()
            )
        }

        return OkHttpClient.Builder()
            .readTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(apiInterceptor)
            .addInterceptor(interceptor)
            .addInterceptor(ConnectivityInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
