package ru.nyxsed.postscan.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nyxsed.postscan.Util.Constants.VK_BASE_URL
import ru.nyxsed.postscan.data.network.ApiService

val networkModule = module {
    // Retrofit dependencies
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(VK_BASE_URL)  // Указываем свой базовый URL
            .addConverterFactory(GsonConverterFactory.create())  // Выбираем конвертер (например, Gson)
            .client(get())
            .build()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)  // Создаем экземпляр ApiService с помощью Retrofit
    }
}