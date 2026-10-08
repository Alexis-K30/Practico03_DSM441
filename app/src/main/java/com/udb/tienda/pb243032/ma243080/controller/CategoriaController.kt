package com.udb.tienda.pb243032.ma243080.controller

import com.udb.tienda.pb243032.ma243080.model.inventario.Categoria
import com.udb.tienda.pb243032.ma243080.model.inventario.CategoriaRepository

// MÉTODO PRINCIPAL: recibe las acciones de la vista relacionadas a las categorías del inventario,
// las delega al repositorio, y notifica a la vista el resultado
class CategoriaController(private val vista: CategoriaVista) {

    private val repositorio = CategoriaRepository()

    interface CategoriaVista {
        fun mostrarCategorias(categorias: List<Categoria>)
        fun categoriaGuardada(categoria: Categoria)
        fun categoriaActualizada(categoria: Categoria)
        fun categoriaEliminada()
        fun mostrarError(mensaje: String)
    }

    fun cargarCategorias() {
        repositorio.listarCategorias(
            alExito = { categorias -> vista.mostrarCategorias(categorias) },
            alError = { mensaje -> vista.mostrarError(mensaje) }
        )
    }

    fun crearCategoria(categoria: Categoria) {
        repositorio.crearCategoria(
            categoria = categoria,
            alExito = { creada -> vista.categoriaGuardada(creada) },
            alError = { mensaje -> vista.mostrarError(mensaje) }
        )
    }

    fun actualizarCategoria(id: String, categoria: Categoria) {
        repositorio.actualizarCategoria(
            id = id,
            categoria = categoria,
            alExito = { actualizada -> vista.categoriaActualizada(actualizada) },
            alError = { mensaje -> vista.mostrarError(mensaje) }
        )
    }

    fun eliminarCategoria(id: String) {
        repositorio.eliminarCategoria(
            id = id,
            alExito = { vista.categoriaEliminada() },
            alError = { mensaje -> vista.mostrarError(mensaje) }
        )
    }
}