package com.udb.tienda.pb243032.ma243080.model.inventario

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import java.io.IOException

// MÉTODO PRINCIPAL: consulta el listado de productos usando directamente OkHttpClient y Corrutinas,
// y realiza el parseo manual del JSON con JSONArray y JSONObject (sin Gson)
class ReporteRepository {

    companion object {
        private const val URL_INVENTARIO_PRODUCTO = "https://6ac5954d54a61668c5f74125.mockapi.io/api/v1/producto"
    }

    private val loggingInterceptor = HttpLoggingInterceptor { mensaje ->
        Log.d("TiendaButiti_OkHttp", mensaje)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    suspend fun obtenerProductosConOkHttp(): List<Producto> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(URL_INVENTARIO_PRODUCTO)
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()

        Log.d("TiendaButiti_OkHttp", "Reporte HTTP Status Code: ${response.code}")

        if (!response.isSuccessful) {
            throw IOException("Error al consultar el reporte de inventario: código ${response.code}")
        }

        val jsonString = response.body?.string() ?: ""
        if (jsonString.isBlank()) {
            return@withContext emptyList()
        }

        // Conversión manual del JSON recibido a objetos del modelo usando JSONArray y JSONObject
        val listaProductos = mutableListOf<Producto>()
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.optString("id", "")
            val nombre = jsonObject.optString("nombre", "")
            val descripcion = jsonObject.optString("descripcion", "")
            val precio = jsonObject.optDouble("precio", 0.0)
            val stock = jsonObject.optInt("stock", 0)
            val categoria = jsonObject.optString("categoria", "")

            listaProductos.add(
                Producto(
                    id = id,
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio,
                    stock = stock,
                    categoria = categoria
                )
            )
        }

        listaProductos
    }
}