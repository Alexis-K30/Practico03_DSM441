package com.udb.tienda.pb243032.ma243080.model.inventario

import com.udb.tienda.pb243032.ma243080.model.mensajeDeFalla
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// MÉTODO PRINCIPAL: única clase responsable de comunicarse con la API propia para el recurso Categoria
class CategoriaRepository {

    private val apiService = InventarioRetrofitCliente.inventarioApiService

    fun listarCategorias(alExito: (List<Categoria>) -> Unit, alError: (String) -> Unit) {
        apiService.listarCategorias().enqueue(object : Callback<List<Categoria>> {
            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                if (response.isSuccessful) {
                    alExito(response.body() ?: emptyList())
                } else {
                    alError("Error al listar categorías: código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                alError(mensajeDeFalla(t))
            }
        })
    }

    fun crearCategoria(categoria: Categoria, alExito: (Categoria) -> Unit, alError: (String) -> Unit) {
        apiService.crearCategoria(categoria).enqueue(object : Callback<Categoria> {
            override fun onResponse(call: Call<Categoria>, response: Response<Categoria>) {
                if (response.isSuccessful && response.body() != null) {
                    alExito(response.body()!!)
                } else {
                    alError("Error al crear categoría: código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Categoria>, t: Throwable) {
                alError(mensajeDeFalla(t))
            }
        })
    }

    fun actualizarCategoria(id: String, categoria: Categoria, alExito: (Categoria) -> Unit, alError: (String) -> Unit) {
        apiService.actualizarCategoria(id, categoria).enqueue(object : Callback<Categoria> {
            override fun onResponse(call: Call<Categoria>, response: Response<Categoria>) {
                if (response.isSuccessful && response.body() != null) {
                    alExito(response.body()!!)
                } else {
                    alError("Error al actualizar categoría: código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Categoria>, t: Throwable) {
                alError(mensajeDeFalla(t))
            }
        })
    }

    fun eliminarCategoria(id: String, alExito: () -> Unit, alError: (String) -> Unit) {
        apiService.eliminarCategoria(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    alExito()
                } else {
                    alError("Error al eliminar categoría: código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                alError(mensajeDeFalla(t))
            }
        })
    }
}