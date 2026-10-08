package com.udb.tienda.pb243032.ma243080.model.inventario

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// MÉTODO PRINCIPAL: única clase responsable de comunicarse con la API propia para el recurso Producto
class ProductoRepository {

    private val apiService = InventarioRetrofitCliente.inventarioApiService

    fun listarProductos(alExito: (List<Producto>) -> Unit, alError: (String) -> Unit) {
        apiService.listarProductos().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    alExito(response.body() ?: emptyList())
                } else {
                    alError("Error al listar productos: código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                alError("Fallo de conexión: ${t.message}")
            }
        })
    }

    fun obtenerProductoPorId(id: String, alExito: (Producto) -> Unit, alNoEncontrado: () -> Unit, alError: (String) -> Unit) {
        apiService.obtenerProductoPorId(id).enqueue(object : Callback<Producto> {
            override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                when {
                    response.isSuccessful && response.body() != null -> alExito(response.body()!!)
                    response.code() == 404 -> alNoEncontrado()
                    else -> alError("Error al buscar producto: código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Producto>, t: Throwable) {
                alError("Fallo de conexión: ${t.message}")
            }
        })
    }

    fun crearProducto(producto: Producto, alExito: (Producto) -> Unit, alError: (String) -> Unit) {
        apiService.crearProducto(producto).enqueue(object : Callback<Producto> {
            override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                if (response.isSuccessful && response.body() != null) {
                    alExito(response.body()!!)
                } else {
                    alError("Error al crear producto: código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Producto>, t: Throwable) {
                alError("Fallo de conexión: ${t.message}")
            }
        })
    }

    fun actualizarProducto(id: String, producto: Producto, alExito: (Producto) -> Unit, alError: (String) -> Unit) {
        apiService.actualizarProducto(id, producto).enqueue(object : Callback<Producto> {
            override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                if (response.isSuccessful && response.body() != null) {
                    alExito(response.body()!!)
                } else {
                    alError("Error al actualizar producto: código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Producto>, t: Throwable) {
                alError("Fallo de conexión: ${t.message}")
            }
        })
    }

    fun eliminarProducto(id: String, alExito: () -> Unit, alError: (String) -> Unit) {
        apiService.eliminarProducto(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    alExito()
                } else {
                    alError("Error al eliminar producto: código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                alError("Fallo de conexión: ${t.message}")
            }
        })
    }
}