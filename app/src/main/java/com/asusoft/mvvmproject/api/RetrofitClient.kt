package com.asusoft.mvvmproject.api

import com.asusoft.mvvmproject.api.member.MemberService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.logger.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {

    const val LOCAL_URL = "http://172.30.1.10:8080/"

    private var url = LOCAL_URL
    private var API_KEY = "***"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRxJava(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }

        return if (BuildConfig.DEBUG) {
            okHttpClientBuilder
                .addInterceptor(requestInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
        } else {
            okHttpClientBuilder
                .build()
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson, rxjava: RxJava2CallAdapterFactory): Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(rxjava)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

    @Singleton
    @Provides
    fun provideMemberService(retrofit: Retrofit): MemberService = retrofit.create(MemberService::class.java)
}