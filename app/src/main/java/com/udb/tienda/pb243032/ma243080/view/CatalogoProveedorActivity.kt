package com.udb.tienda.pb243032.ma243080.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.udb.tienda.pb243032.ma243080.controller.CatalogoController
import com.udb.tienda.pb243032.ma243080.databinding.ActivityCatalogoProveedorBinding
import com.udb.tienda.pb243032.ma243080.model.proveedor.ProductoProveedor
import androidx.activity.enableEdgeToEdge

// MÉTODO PRINCIPAL: pantalla principal, muestra el catálogo del proveedor (DummyJSON)
class CatalogoProveedorActivity : AppCompatActivity(), CatalogoController.CatalogoVista {

    private lateinit var binding: ActivityCatalogoProveedorBinding
    private lateinit var controller: CatalogoController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCatalogoProveedorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarBarrasDelSistema(binding.rootMain)

        controller = CatalogoController(this)

        configurarRecyclerView()
        configurarBusqueda()

        mostrarCargando(true)
        controller.cargarProductos()
    }

    private fun configurarRecyclerView() {
        binding.rvCatalogo.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarBusqueda() {
        binding.searchViewCatalogo.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mostrarCargando(true)
                controller.buscarProductos(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // No buscamos en cada letra para no saturar la API; solo al confirmar (submit)
                return false
            }
        })
    }

    override fun mostrarProductos(productos: List<ProductoProveedor>) {
        mostrarCargando(false)

        if (productos.isEmpty()) {
            binding.tvSinResultados.visibility = View.VISIBLE
            binding.rvCatalogo.visibility = View.GONE
            return
        }

        binding.tvSinResultados.visibility = View.GONE
        binding.rvCatalogo.visibility = View.VISIBLE

        binding.rvCatalogo.adapter = CatalogoAdapter(productos) { productoSeleccionado ->
            importarProductoAlInventario(productoSeleccionado)
        }
    }

    override fun mostrarError(mensaje: String) {
        mostrarCargando(false)
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun mostrarCargando(mostrar: Boolean) {
        binding.progressCatalogo.visibility = if (mostrar) View.VISIBLE else View.GONE
        if (mostrar) {
            binding.tvSinResultados.visibility = View.GONE
        }
    }

    private fun importarProductoAlInventario(producto: ProductoProveedor) {
        val intent = Intent(this, CrearProductoActivity::class.java)
        intent.putExtra("nombre_precargado", producto.title)
        intent.putExtra("descripcion_precargada", producto.description)
        intent.putExtra("precio_precargado", producto.price)
        startActivity(intent)
    }
}