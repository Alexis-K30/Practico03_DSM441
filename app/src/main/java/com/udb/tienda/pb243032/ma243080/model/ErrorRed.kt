package com.udb.tienda.pb243032.ma243080.model

import android.util.Log
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

// MÉTODO PRINCIPAL: traduce una falla de red (Throwable) a un mensaje claro para el usuario
fun mensajeDeFalla(t: Throwable): String {
    // El detalle técnico queda en el Logcat, el usuario solo ve el mensaje claro
    Log.d("TiendaButiti_HTTP", "Falla de red: $t")

    return when (t) {
        is UnknownHostException, is ConnectException ->
            "Sin conexión a internet. Revisá tu red e intentá de nuevo"
        is SocketTimeoutException ->
            "El servidor tardó demasiado en responder. Intentá de nuevo"
        else ->
            "No se pudo completar la solicitud. Intentá de nuevo"
    }
}