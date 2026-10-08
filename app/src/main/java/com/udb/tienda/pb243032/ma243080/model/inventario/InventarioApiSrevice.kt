package com.udb.tienda.pb243032.ma243080.model.inventario

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.Path

// MÉTODO PRINCIPAL: define las peticiones HTTP disponibles contra la API propia (MockAPI.io)
interface InventarioApiService {

    // --- Producto ---
    @GET("producto")
    fun listarProductos(): Call<List<Producto>>

    @GET("producto/{id}")
    fun obtenerProductoPorId(@Path("id") id: String): Call<Producto>

    @POST("producto")
    fun crearProducto(@Body producto: Producto): Call<Producto>

    @PUT("producto/{id}")
    fun actualizarProducto(@Path("id") id: String, @Body producto: Producto): Call<Producto>

    @DELETE("producto/{id}")
    fun eliminarProducto(@Path("id") id: String): Call<Void>

    // --- Categoria ---
    @GET("categoria")
    fun listarCategorias(): Call<List<Categoria>>

    @POST("categoria")
    fun crearCategoria(@Body categoria: Categoria): Call<Categoria>

    @PUT("categoria/{id}")
    fun actualizarCategoria(@Path("id") id: String, @Body categoria: Categoria): Call<Categoria>

    @DELETE("categoria/{id}")
    fun eliminarCategoria(@Path("id") id: String): Call<Void>
}