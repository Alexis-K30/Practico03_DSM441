package com.udb.tienda.pb243032.ma243080.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.udb.tienda.pb243032.ma243080.controller.ReporteController
import com.udb.tienda.pb243032.ma243080.databinding.ActivityReporteInventarioBinding
import com.udb.tienda.pb243032.ma243080.model.inventario.Producto
import java.util.Locale

// MÉTODO PRINCIPAL: pantalla de reporte de inventario que consume datos con OkHttp y Corrutinas
class ReporteInventarioActivity : AppCompatActivity(), ReporteController.ReporteVista {

    private lateinit var binding: ActivityReporteInventarioBinding
    private lateinit var controller: ReporteController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReporteInventarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarBarrasDelSistema(binding.rootReporte)

        controller = ReporteController(this)

        binding.rvStockBajo.layoutManager = LinearLayoutManager(this)

        binding.btnVolverReporte.setOnClickListener {
            finish()
        }

        mostrarCargando(true)
        controller.generarReporte()
    }

    override fun mostrarReporte(
        totalProductos: Int,
        valorTotalInventario: Double,
        productosStockBajo: List<Producto>
    ) {
        mostrarCargando(false)

        binding.tvTotalProductosVal.text = totalProductos.toString()
        binding.tvValorTotalVal.text = String.format(Locale.US, "$%.2f", valorTotalInventario)

        if (productosStockBajo.isEmpty()) {
            binding.tvSinStockBajo.visibility = View.VISIBLE
            binding.rvStockBajo.visibility = View.GONE
        } else {
            binding.tvSinStockBajo.visibility = View.GONE
            binding.rvStockBajo.visibility = View.VISIBLE
            binding.rvStockBajo.adapter = ReporteAdapter(productosStockBajo)
        }
    }

    override fun mostrarError(mensaje: String) {
        mostrarCargando(false)
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun mostrarCargando(mostrar: Boolean) {
        binding.progressReporte.visibility = if (mostrar) View.VISIBLE else View.GONE
        if (mostrar) {
            binding.tvSinStockBajo.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.onDestroy()
    }
}