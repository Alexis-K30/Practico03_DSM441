package com.udb.tienda.pb243032.ma243080.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udb.tienda.pb243032.ma243080.R
import com.udb.tienda.pb243032.ma243080.controller.CategoriaController
import com.udb.tienda.pb243032.ma243080.controller.ProductoController
import com.udb.tienda.pb243032.ma243080.databinding.ActivityActualizarProductoBinding
import com.udb.tienda.pb243032.ma243080.model.inventario.Categoria
import com.udb.tienda.pb243032.ma243080.model.inventario.Producto
import androidx.activity.enableEdgeToEdge

// MÉTODO PRINCIPAL: formulario para editar un producto existente del inventario propio
class ActualizarProductoActivity : AppCompatActivity(),
    ProductoController.ProductoVista,
    CategoriaController.CategoriaVista {

    private lateinit var binding: ActivityActualizarProductoBinding
    private lateinit var productoController: ProductoController
    private lateinit var categoriaController: CategoriaController
    private var listaCategorias: List<Categoria> = emptyList()
    private lateinit var productoIdActual: String
    private var categoriaActualDelProducto: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityActualizarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarBarrasDelSistema(binding.rootActualizarProducto)

        productoController = ProductoController(this)
        categoriaController = CategoriaController(this)

        productoIdActual = intent.getStringExtra("producto_id") ?: ""

        if (productoIdActual.isBlank()) {
            Toast.makeText(this, getString(R.string.error_generico_catalogo), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Precargamos los datos actuales del producto a editar
        binding.etNombreActualizar.setText(intent.getStringExtra("nombre"))
        binding.etDescripcionActualizar.setText(intent.getStringExtra("descripcion"))
        binding.etPrecioActualizar.setText(intent.getDoubleExtra("precio", 0.0).toString())
        binding.etStockActualizar.setText(intent.getIntExtra("stock", 0).toString())
        categoriaActualDelProducto = intent.getStringExtra("categoria") ?: ""

        categoriaController.cargarCategorias()

        binding.btnActualizarProducto.setOnClickListener {
            intentarActualizarProducto()
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
        binding.spinnerCategoriaActualizar.adapter = adapter

        // Seleccionamos automáticamente la categoría que ya tenía el producto
        val posicionActual = nombresCategorias.indexOf(categoriaActualDelProducto)
        if (posicionActual >= 0) {
            binding.spinnerCategoriaActualizar.setSelection(posicionActual)
        }
    }

    private fun intentarActualizarProducto() {
        val nombre = binding.etNombreActualizar.text.toString().trim()
        val descripcion = binding.etDescripcionActualizar.text.toString().trim()
        val precioTexto = binding.etPrecioActualizar.text.toString().trim()
        val stockTexto = binding.etStockActualizar.text.toString().trim()

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

        val categoriaSeleccionada = listaCategorias[binding.spinnerCategoriaActualizar.selectedItemPosition].nombre

        val productoActualizado = Producto(
            id = productoIdActual,
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            stock = stock,
            categoria = categoriaSeleccionada
        )

        mostrarCargando(true)
        productoController.actualizarProducto(productoIdActual, productoActualizado)
    }

    override fun productoActualizado(producto: Producto) {
        mostrarCargando(false)
        Toast.makeText(this, getString(R.string.producto_actualizado_exito), Toast.LENGTH_SHORT).show()
        finish()
    }

    // --- No se usan en esta pantalla, pero la interfaz los exige ---
    override fun mostrarProductos(productos: List<Producto>) {}
    override fun productoGuardado(producto: Producto) {}
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
        binding.progressActualizar.visibility = if (mostrar) View.VISIBLE else View.GONE
        binding.btnActualizarProducto.isEnabled = !mostrar
    }
}