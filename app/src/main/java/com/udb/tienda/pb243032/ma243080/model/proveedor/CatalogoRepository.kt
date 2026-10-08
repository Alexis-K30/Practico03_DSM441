package com.udb.tienda.pb243032.ma243080.model.proveedor

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// MÉTODO PRINCIPAL: única clase responsable de comunicarse con la API del catálogo del proveedor
class CatalogoRepository {

    private val apiService = RetrofitCliente.catalogoApiService

    fun obtenerProductos(
        alExito: (List<ProductoProveedor>) -> Unit,
        alError: (String) -> Unit
    ) {
        apiService.obtenerProductos().enqueue(object : Callback<CatalogoRespuesta> {
            override fun onResponse(call: Call<CatalogoRespuesta>, response: Response<CatalogoRespuesta>) {
                if (response.isSuccessful) {
                    val productos = response.body()?.productos ?: emptyList()
                    alExito(productos)
                } else {
                    alError("Error del servidor: código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CatalogoRespuesta>, t: Throwable) {
                alError("Fallo de conexión: ${t.message}")
            }
        })
    }

    fun buscarProductos(
        consulta: String,
        alExito: (List<ProductoProveedor>) -> Unit,
        alError: (String) -> Unit
    ) {
        apiService.buscarProductos(consulta).enqueue(object : Callback<CatalogoRespuesta> {
            override fun onResponse(call: Call<CatalogoRespuesta>, response: Response<CatalogoRespuesta>) {
                if (response.isSuccessful) {
                    val productos = response.body()?.productos ?: emptyList()
                    alExito(productos)
                } else {
                    alError("Error del servidor: código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CatalogoRespuesta>, t: Throwable) {
                alError("Fallo de conexión: ${t.message}")
            }
        })
    }
}