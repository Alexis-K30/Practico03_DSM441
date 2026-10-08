package com.udb.tienda.pb243032.ma243080.model.inventario

// MÉTODO PRINCIPAL: representa un producto del inventario propio (API en MockAPI.io)
data class Producto(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val stock: Int = 0,
    val categoria: String = ""
)