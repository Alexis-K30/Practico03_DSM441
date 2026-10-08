package com.udb.tienda.pb243032.ma243080.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.udb.tienda.pb243032.ma243080.controller.ProductoController
import com.udb.tienda.pb243032.ma243080.databinding.ActivityMainBinding
import com.udb.tienda.pb243032.ma243080.model.inventario.Producto
import com.udb.tienda.pb243032.ma243080.R
import androidx.activity.enableEdgeToEdge

// MÉTODO PRINCIPAL: pantalla principal de la app, muestra el inventario propio de Tienda Butiti
class MainActivity : AppCompatActivity(), ProductoController.ProductoVista {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: ProductoController
    private var productoParaAccion: Producto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarBarrasDelSistema(binding.rootMain)

        controller = ProductoController(this)

        binding.rvInventario.layoutManager = LinearLayoutManager(this)

        binding.btnIrCatalogoProveedor.setOnClickListener {
            startActivity(Intent(this, CatalogoProveedorActivity::class.java))
        }

        binding.btnCrearProducto.setOnClickListener {
            startActivity(Intent(this, CrearProductoActivity::class.java))
        }

        binding.btnIrCategorias.setOnClickListener {
            startActivity(Intent(this, CategoriasActivity::class.java))
        }

        // btnIrReporte y btnBuscarPorId los conectamos más adelante,
        // cuando armemos ReporteInventarioActivity y la búsqueda por id
    }

    override fun onResume() {
        super.onResume()
        mostrarCargando(true)
        controller.cargarProductos()
    }

    override fun mostrarProductos(productos: List<Producto>) {
        mostrarCargando(false)

        if (productos.isEmpty()) {
            binding.tvSinProductos.visibility = View.VISIBLE
            binding.rvInventario.visibility = View.GONE
            return
        }

        binding.tvSinProductos.visibility = View.GONE
        binding.rvInventario.visibility = View.VISIBLE

        binding.rvInventario.adapter = ProductoAdapter(productos) { productoSeleccionado ->
            mostrarDialogoModificarEliminar(productoSeleccionado)
        }
    }

    private fun mostrarDialogoModificarEliminar(producto: Producto) {
        productoParaAccion = producto

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialogo_producto_titulo))
            .setMessage(producto.nombre)
            .setPositiveButton(getString(R.string.dialogo_producto_modificar)) { _, _ ->
                val intent = Intent(this, ActualizarProductoActivity::class.java)
                intent.putExtra("producto_id", producto.id)
                intent.putExtra("nombre", producto.nombre)
                intent.putExtra("descripcion", producto.descripcion)
                intent.putExtra("precio", producto.precio)
                intent.putExtra("stock", producto.stock)
                intent.putExtra("categoria", producto.categoria)
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.dialogo_producto_eliminar)) { _, _ ->
                confirmarEliminarProducto(producto)
            }
            .show()
    }

    private fun confirmarEliminarProducto(producto: Producto) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmar_eliminar_producto_titulo))
            .setMessage(getString(R.string.confirmar_eliminar_producto_mensaje))
            .setPositiveButton(getString(R.string.confirmar_eliminar_si)) { _, _ ->
                mostrarCargando(true)
                controller.eliminarProducto(producto.id)
            }
            .setNegativeButton(getString(R.string.confirmar_eliminar_no), null)
            .show()
    }

    override fun productoEliminado() {
        Toast.makeText(this, getString(R.string.producto_eliminado_exito), Toast.LENGTH_SHORT).show()
        controller.cargarProductos() // refresca la lista automáticamente
    }

    // --- Estos tres no se usan en MainActivity, pero la interfaz ProductoVista los exige ---
    override fun productoGuardado(producto: Producto) {}
    override fun productoActualizado(producto: Producto) {}
    override fun productoEncontrado(producto: Producto) {}
    override fun productoNoEncontrado() {}

    override fun mostrarError(mensaje: String) {
        mostrarCargando(false)
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun mostrarCargando(mostrar: Boolean) {
        binding.progressMain.visibility = if (mostrar) View.VISIBLE else View.GONE
        if (mostrar) {
            binding.tvSinProductos.visibility = View.GONE
        }
    }
}