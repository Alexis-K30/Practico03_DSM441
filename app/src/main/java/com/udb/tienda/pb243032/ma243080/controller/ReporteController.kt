package com.udb.tienda.pb243032.ma243080.controller

import com.udb.tienda.pb243032.ma243080.model.inventario.Producto
import com.udb.tienda.pb243032.ma243080.model.inventario.ReporteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// MÉTODO PRINCIPAL: recibe la solicitud de la vista de reporte, solicita la lista de productos
// mediante el repositorio con OkHttp/Corrutinas, calcula los totales y productos con stock bajo,
// y notifica el resultado a la vista
class ReporteController(private val vista: ReporteVista) {

    private val repositorio = ReporteRepository()
    private val controllerJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + controllerJob)

    interface ReporteVista {
        fun mostrarReporte(
            totalProductos: Int,
            valorTotalInventario: Double,
            productosStockBajo: List<Producto>
        )
        fun mostrarError(mensaje: String)
    }

    fun generarReporte() {
        scope.launch {
            try {
                val productos = repositorio.obtenerProductosConOkHttp()

                val totalProductos = productos.size
                val valorTotalInventario = productos.sumOf { it.precio * it.stock }
                val productosStockBajo = productos.filter { it.stock < 5 }

                vista.mostrarReporte(totalProductos, valorTotalInventario, productosStockBajo)
            } catch (e: Exception) {
                vista.mostrarError(e.message ?: "Error al generar el reporte de inventario")
            }
        }
    }

    fun onDestroy() {
        controllerJob.cancel()
    }
}