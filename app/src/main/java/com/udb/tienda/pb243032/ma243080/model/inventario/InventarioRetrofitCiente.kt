package com.udb.tienda.pb243032.ma243080.model.inventario

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// MÉTODO PRINCIPAL: configura y expone la instancia de Retrofit para la API propia (MockAPI.io)
object InventarioRetrofitCliente {

    private const val URL_BASE_INVENTARIO = "https://6ac5954d54a61668c5f74125.mockapi.io/api/v1/"

    private val loggingInterceptor = HttpLoggingInterceptor(
        HttpLoggingInterceptor.Logger { mensaje ->
            android.util.Log.d("TiendaButiti_HTTP", mensaje)
        }
    ).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val inventarioApiService: InventarioApiService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE_INVENTARIO)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InventarioApiService::class.java)
    }
}