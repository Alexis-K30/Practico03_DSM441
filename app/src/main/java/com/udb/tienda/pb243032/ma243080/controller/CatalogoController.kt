package com.udb.tienda.pb243032.ma243080.controller

import com.udb.tienda.pb243032.ma243080.model.proveedor.CatalogoRepository
import com.udb.tienda.pb243032.ma243080.model.proveedor.ProductoProveedor

// MÉTODO PRINCIPAL: recibe las acciones de la vista relacionadas al catálogo del proveedor,
// las delega al repositorio, y notifica a la vista el resultado
class CatalogoController(private val vista: CatalogoVista) {

    private val repositorio = CatalogoRepository()

    // Contrato que la Activity debe implementar para recibir los resultados
    interface CatalogoVista {
        fun mostrarProductos(productos: List<ProductoProveedor>)
        fun mostrarError(mensaje: String)
    }

    fun cargarProductos() {
        repositorio.obtenerProductos(
            alExito = { productos -> vista.mostrarProductos(productos) },
            alError = { mensaje -> vista.mostrarError(mensaje) }
        )
    }

    fun buscarProductos(consulta: String) {
        if (consulta.isBlank()) {
            cargarProductos()
            return
        }

        repositorio.buscarProductos(
            consulta = consulta,
            alExito = { productos -> vista.mostrarProductos(productos) },
            alError = { mensaje -> vista.mostrarError(mensaje) }
        )
    }
}