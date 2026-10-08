package com.udb.tienda.pb243032.ma243080.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.udb.tienda.pb243032.ma243080.R
import com.udb.tienda.pb243032.ma243080.controller.CategoriaController
import com.udb.tienda.pb243032.ma243080.controller.ProductoController
import com.udb.tienda.pb243032.ma243080.databinding.ActivityCrearProductoBinding
import com.udb.tienda.pb243032.ma243080.model.inventario.Categoria
import com.udb.tienda.pb243032.ma243080.model.inventario.Producto

// MÉTODO PRINCIPAL: formulario para crear un nuevo producto en el inventario propio
class CrearProductoActivity : AppCompatActivity(),
    ProductoController.ProductoVista,
    CategoriaController.CategoriaVista {

    private lateinit var binding: ActivityCrearProductoBinding
    private lateinit var productoController: ProductoController
    private lateinit var categoriaController: CategoriaController
    private var listaCategorias: List<Categoria> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCrearProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarBarrasDelSistema(binding.rootCrearProducto)

        productoController = ProductoController(this)
        categoriaController = CategoriaController(this)

        // Precargamos datos si venimos desde la importación del catálogo del proveedor
        val nombrePrecargado = intent.getStringExtra("nombre_precargado")
        val descripcionPrecargada = intent.getStringExtra("descripcion_precargada")
        val precioPrecargado = intent.getDoubleExtra("precio_precargado", -1.0)

        if (!nombrePrecargado.isNullOrBlank()) {
            binding.etNombreProducto.setText(nombrePrecargado)
        }
        if (!descripcionPrecargada.isNullOrBlank()) {
            binding.etDescripcionProducto.setText(descripcionPrecargada)
        }
        if (precioPrecargado > 0) {
            binding.etPrecioProducto.setText(precioPrecargado.toString())
        }

        categoriaController.cargarCategorias()

        binding.btnGuardarProducto.setOnClickListener {
            intentarGuardarProducto()
        }
    }

    override fun mostrarCategorias(categorias: List<Categoria>) {
        listaCategorias = categorias
        val nombresCategorias = categorias.map { it.nombre }

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item_categoria,
            nombresCategorias
        )
        binding.spinnerCategoria.adapter = adapter
    }

    private fun intentarGuardarProducto() {
        val nombre = binding.etNombreProducto.text.toString().trim()
        val descripcion = binding.etDescripcionProducto.text.toString().trim()
        val precioTexto = binding.etPrecioProducto.text.toString().trim()
        val stockTexto = binding.etStockProducto.text.toString().trim()

        if (nombre.isBlank()) {
            Toast.makeText(this, getString(R.string.error_nombre_vacio), Toast.LENGTH_SHORT).show()
            return
        }

        if (descripcion.isBlank()) {
            Toast.makeText(this, getString(R.string.error_descripcion_vacia), Toast.LENGTH_SHORT).show()
            return
        }

        val precio = precioTexto.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            Toast.makeText(this, getString(R.string.error_precio_invalido), Toast.LENGTH_SHORT).show()
            return
        }

        val stock = stockTexto.toIntOrNull()
        if (stock == null || stock < 0) {
            Toast.makeText(this, getString(R.string.error_stock_invalido), Toast.LENGTH_SHORT).show()
            return
        }

        if (listaCategorias.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_sin_categoria), Toast.LENGTH_SHORT).show()
            return
        }

        val categoriaSeleccionada = listaCategorias[binding.spinnerCategoria.selectedItemPosition].nombre

        val nuevoProducto = Producto(
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            stock = stock,
            categoria = categoriaSeleccionada
        )

        mostrarCargando(true)
        productoController.crearProducto(nuevoProducto)
    }

    override fun productoGuardado(producto: Producto) {
        mostrarCargando(false)
        Toast.makeText(this, getString(R.string.producto_creado_exito), Toast.LENGTH_SHORT).show()
        finish()
    }

    // --- No se usan en esta pantalla, pero la interfaz los exige ---
    override fun mostrarProductos(productos: List<Producto>) {}
    override fun productoActualizado(producto: Producto) {}
    override fun productoEliminado() {}
    override fun productoEncontrado(producto: Producto) {}
    override fun productoNoEncontrado() {}
    override fun categoriaGuardada(categoria: Categoria) {}
    override fun categoriaActualizada(categoria: Categoria) {}
    override fun categoriaEliminada() {}

    override fun mostrarError(mensaje: String) {
        mostrarCargando(false)
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun mostrarCargando(mostrar: Boolean) {
        binding.progressCrear.visibility = if (mostrar) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnGuardarProducto.isEnabled = !mostrar
    }
}