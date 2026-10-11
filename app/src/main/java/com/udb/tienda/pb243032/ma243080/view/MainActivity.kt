package com.udb.tienda.pb243032.ma243080.view

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.udb.tienda.pb243032.ma243080.R
import com.udb.tienda.pb243032.ma243080.controller.ProductoController
import com.udb.tienda.pb243032.ma243080.databinding.ActivityMainBinding
import com.udb.tienda.pb243032.ma243080.model.inventario.Producto

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

        binding.btnIrReporte.setOnClickListener {
            startActivity(Intent(this, ReporteInventarioActivity::class.java))
        }

        binding.btnBuscarPorId.setOnClickListener {
            mostrarDialogoBuscarPorId()
        }
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

    private fun mostrarDialogoBuscarPorId() {
        val input = EditText(this).apply {
            hint = getString(R.string.hint_ingresar_id)
            inputType = InputType.TYPE_CLASS_NUMBER
            setPadding(50, 40, 50, 40)
        }

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.titulo_buscar_id_dialogo))
            .setView(input)
            .setPositiveButton(getString(R.string.boton_buscar)) { _, _ ->
                val idIngresado = input.text.toString().trim()
                if (idIngresado.isBlank()) {
                    Toast.makeText(this, getString(R.string.error_id_vacio), Toast.LENGTH_SHORT).show()
                } else {
                    mostrarCargando(true)
                    controller.buscarPorId(idIngresado)
                }
            }
            .setNegativeButton(getString(R.string.confirmar_eliminar_no), null)
            .show()
    }

    override fun productoEncontrado(producto: Producto) {
        mostrarCargando(false)

        val detalle = """
            ID: ${producto.id}
            Nombre: ${producto.nombre}
            Descripción: ${producto.descripcion}
            Precio: $${producto.precio}
            Stock: ${producto.stock} unidades
            Categoría: ${producto.categoria}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.detalle_producto_titulo))
            .setMessage(detalle)
            .setPositiveButton(getString(R.string.dialogo_producto_modificar)) { _, _ ->
                val intent = Intent(this, ActualizarProductoActivity::class.java).apply {
                    putExtra("producto_id", producto.id)
                    putExtra("nombre", producto.nombre)
                    putExtra("descripcion", producto.descripcion)
                    putExtra("precio", producto.precio)
                    putExtra("stock", producto.stock)
                    putExtra("categoria", producto.categoria)
                }
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.dialogo_producto_eliminar)) { _, _ ->
                confirmarEliminarProducto(producto)
            }
            .setNeutralButton(getString(R.string.boton_volver), null)
            .show()
    }

    override fun productoNoEncontrado() {
        mostrarCargando(false)
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.titulo_buscar_id_dialogo))
            .setMessage(getString(R.string.producto_no_encontrado_msg))
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun mostrarDialogoModificarEliminar(producto: Producto) {
        productoParaAccion = producto

        AlertDialog.Builder(this)
            .setTitle(producto.nombre)
            .setMessage("ID: ${producto.id}\nCategoría: ${producto.categoria}\nPrecio: $${producto.precio}\nStock: ${producto.stock} unidades")
            .setPositiveButton(getString(R.string.dialogo_producto_modificar)) { _, _ ->
                val intent = Intent(this, ActualizarProductoActivity::class.java).apply {
                    putExtra("producto_id", producto.id)
                    putExtra("nombre", producto.nombre)
                    putExtra("descripcion", producto.descripcion)
                    putExtra("precio", producto.precio)
                    putExtra("stock", producto.stock)
                    putExtra("categoria", producto.categoria)
                }
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

    // --- No se usan en MainActivity, pero la interfaz ProductoVista los exige ---
    override fun productoGuardado(producto: Producto) {}
    override fun productoActualizado(producto: Producto) {}

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