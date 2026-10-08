package com.udb.tienda.pb243032.ma243080.view

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.udb.tienda.pb243032.ma243080.R

// MÉTODO PRINCIPAL: reserva espacio para la barra de notificaciones, la cámara (notch) y la barra de
// navegación, y deja los íconos del sistema en oscuro para que se lean sobre el fondo claro de la app
fun AppCompatActivity.configurarBarrasDelSistema(raiz: View) {
    val paddingGeneral = resources.getDimensionPixelSize(R.dimen.padding_general)

    ViewCompat.setOnApplyWindowInsetsListener(raiz) { vista, insets ->
        val barras = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        Log.d("TiendaButiti_UI", "Insets aplicados en ${javaClass.simpleName}: top=${barras.top} bottom=${barras.bottom}")
        vista.setPadding(
            paddingGeneral + barras.left,
            paddingGeneral + barras.top,
            paddingGeneral + barras.right,
            paddingGeneral + barras.bottom
        )
        insets
    }

    // Pedimos explícitamente que el sistema entregue los insets ahora
    ViewCompat.requestApplyInsets(raiz)

    val controlador = WindowCompat.getInsetsController(window, raiz)
    controlador.isAppearanceLightStatusBars = true
    controlador.isAppearanceLightNavigationBars = true
}