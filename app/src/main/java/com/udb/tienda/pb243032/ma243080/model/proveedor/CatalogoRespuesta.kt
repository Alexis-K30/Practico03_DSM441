package com.udb.tienda.pb243032.ma243080.model.proveedor

import com.google.gson.annotations.SerializedName

// MÉTODO PRINCIPAL: envuelve la lista de productos que devuelve la API del proveedor
data class CatalogoRespuesta(
    @SerializedName("products")
    val productos: List<ProductoProveedor>,
    val total: Int
)