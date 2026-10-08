package com.udb.tienda.pb243032.ma243080.model.proveedor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// MÉTODO PRINCIPAL: configura y expone la instancia de Retrofit para el catálogo del proveedor (DummyJSON)
object RetrofitCliente {

    private const val URL_BASE_PROVEEDOR = "https://dummyjson.com/"

    val catalogoApiService: CatalogoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE_PROVEEDOR)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatalogoApiService::class.java)
    }
}