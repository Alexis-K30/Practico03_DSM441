package com.udb.tienda.pb243032.ma243080.model.proveedor

// MÉTODO PRINCIPAL: representa un producto tal como lo devuelve la API del proveedor (DummyJSON)
data class ProductoProveedor(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val thumbnail: String
)