package com.udb.tienda.pb243032.ma243080.model.proveedor

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// MÉTODO PRINCIPAL: define las peticiones HTTP disponibles contra la API del proveedor (DummyJSON)
interface CatalogoApiService {

    @GET("products")
    fun obtenerProductos(): Call<CatalogoRespuesta>

    @GET("products/search")
    fun buscarProductos(@Query("q") consulta: String): Call<CatalogoRespuesta>
}