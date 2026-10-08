package com.udb.tienda.pb243032.ma243080.controller

import com.udb.tienda.pb243032.ma243080.model.inventario.Producto
import com.udb.tienda.pb243032.ma243080.model.inventario.ProductoRepository

// MÉTODO PRINCIPAL: recibe las acciones de la vista relacionadas al inventario propio de productos,
// las delega al repositorio, y notifica a la vista el resultado
class ProductoController(private val vista: ProductoVista) {

    private val repositorio = ProductoRepository()

    // Contrato que la Activity debe implementar para recibir los resultados
    interface ProductoVista {
        fun mostrarProductos(productos: List<Producto>)
        fun productoGuardado(producto: Producto)
        fun productoActualizado(producto: Producto)
        fun productoEliminado()
        fun productoEncontrado(producto: Producto)
        fun productoNoEncontrado()
        fun mostrarError(mensaje: String)
    }

    fun cargarProductos() {
        repositorio.listarProductos(
            alExito = { productos -> vista.mostrarProductos(productos) },
            alError = { mensaje -> vista.mostrarError(mensaje) }
        )
    }

    fun buscarPorId(id: String) {
        repositorio.obtenerProductoPorId(
            id = id,
            alExito = { producto -> vista.productoEncontrado(producto) },
            alNoEncontrado = { vista.productoNoEncontrado() },
            alError = { mensaje -> vista.mostrarError(mensaje) }
        )
    }

    fun crearProducto(producto: Producto) {
        repositorio.crearProducto(
            producto = producto,
            alExito = { creado -> vista.productoGuardado(creado) },
            alError = { mensaje -> vista.mostrarError(mensaje) }
        )
    }

    fun actualizarProducto(id: String, producto: Producto) {
        repositorio.actualizarProducto(
            id = id,
            producto = producto,
            alExito = { actualizado -> vista.productoActualizado(actualizado) },
            alError = { mensaje -> vista.mostrarError(mensaje) }
        )
    }

    fun eliminarProducto(id: String) {
        repositorio.eliminarProducto(
            id = id,
            alExito = { vista.productoEliminado() },
            alError = { mensaje -> vista.mostrarError(mensaje) }
        )
    }
}