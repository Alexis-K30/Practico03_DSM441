package com.udb.tienda.pb243032.ma243080.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.udb.tienda.pb243032.ma243080.R
import com.udb.tienda.pb243032.ma243080.controller.CategoriaController
import com.udb.tienda.pb243032.ma243080.databinding.ActivityCategoriasBinding
import com.udb.tienda.pb243032.ma243080.databinding.DialogCategoriaBinding
import com.udb.tienda.pb243032.ma243080.model.inventario.Categoria
import androidx.activity.enableEdgeToEdge

// MÉTODO PRINCIPAL: pantalla del CRUD de categorías del inventario propio
class CategoriasActivity : AppCompatActivity(), CategoriaController.CategoriaVista {

    private lateinit var binding: ActivityCategoriasBinding
    private lateinit var controller: CategoriaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarBarrasDelSistema(binding.rootCategorias)

        controller = CategoriaController(this)

        binding.rvCategorias.layoutManager = LinearLayoutManager(this)

        binding.btnNuevaCategoria.setOnClickListener {
            mostrarDialogoFormulario(null)
        }

        binding.btnVolverCategorias.setOnClickListener {
            finish()
        }

        mostrarCargando(true)
        controller.cargarCategorias()
    }

    override fun mostrarCategorias(categorias: List<Categoria>) {
        mostrarCargando(false)

        if (categorias.isEmpty()) {
            binding.tvSinCategorias.visibility = View.VISIBLE
            binding.rvCategorias.visibility = View.GONE
            return
        }

        binding.tvSinCategorias.visibility = View.GONE
        binding.rvCategorias.visibility = View.VISIBLE

        binding.rvCategorias.adapter = CategoriaAdapter(categorias) { categoriaSeleccionada ->
            mostrarDialogoOpciones(categoriaSeleccionada)
        }
    }

    private fun mostrarDialogoOpciones(categoria: Categoria) {
        AlertDialog.Builder(this)
            .setTitle(categoria.nombre)
            .setMessage(categoria.descripcion)
            .setPositiveButton(getString(R.string.dialogo_producto_modificar)) { _, _ ->
                mostrarDialogoFormulario(categoria)
            }
            .setNegativeButton(getString(R.string.dialogo_producto_eliminar)) { _, _ ->
                confirmarEliminarCategoria(categoria)
            }
            .show()
    }

    // Si categoria es null, el formulario crea una nueva; si no, edita la recibida
    private fun mostrarDialogoFormulario(categoria: Categoria?) {
        val dialogBinding = DialogCategoriaBinding.inflate(layoutInflater)
        val idAEditar = categoria?.id

        if (categoria != null) {
            dialogBinding.etNombreCategoria.setText(categoria.nombre)
            dialogBinding.etDescripcionCategoria.setText(categoria.descripcion)
        }

        val titulo = if (categoria == null) R.string.titulo_nueva_categoria else R.string.titulo_editar_categoria

        val dialogo = AlertDialog.Builder(this)
            .setTitle(getString(titulo))
            .setView(dialogBinding.root)
            .setPositiveButton(getString(R.string.boton_guardar_categoria), null)
            .setNegativeButton(getString(R.string.confirmar_eliminar_no), null)
            .create()

        // Sobrescribimos el click del botón positivo después de mostrar el diálogo,
        // para que NO se cierre automáticamente cuando la validación falla
        dialogo.setOnShowListener {
            dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val nombre = dialogBinding.etNombreCategoria.text.toString().trim()
                val descripcion = dialogBinding.etDescripcionCategoria.text.toString().trim()

                if (nombre.isBlank()) {
                    Toast.makeText(this, getString(R.string.error_nombre_categoria_vacio), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (descripcion.isBlank()) {
                    Toast.makeText(this, getString(R.string.error_descripcion_categoria_vacia), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                dialogo.dismiss()
                mostrarCargando(true)

                if (idAEditar != null) {
                    controller.actualizarCategoria(idAEditar, Categoria(id = idAEditar, nombre = nombre, descripcion = descripcion))
                } else {
                    controller.crearCategoria(Categoria(nombre = nombre, descripcion = descripcion))
                }
            }
        }

        dialogo.show()
    }

    private fun confirmarEliminarCategoria(categoria: Categoria) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmar_eliminar_categoria_titulo))
            .setMessage(getString(R.string.confirmar_eliminar_categoria_mensaje))
            .setPositiveButton(getString(R.string.confirmar_eliminar_si)) { _, _ ->
                mostrarCargando(true)
                controller.eliminarCategoria(categoria.id)
            }
            .setNegativeButton(getString(R.string.confirmar_eliminar_no), null)
            .show()
    }

    override fun categoriaGuardada(categoria: Categoria) {
        Toast.makeText(this, getString(R.string.categoria_guardada_exito), Toast.LENGTH_SHORT).show()
        controller.cargarCategorias() // refresca la lista automáticamente
    }

    override fun categoriaActualizada(categoria: Categoria) {
        Toast.makeText(this, getString(R.string.categoria_actualizada_exito), Toast.LENGTH_SHORT).show()
        controller.cargarCategorias()
    }

    override fun categoriaEliminada() {
        Toast.makeText(this, getString(R.string.categoria_eliminada_exito), Toast.LENGTH_SHORT).show()
        controller.cargarCategorias()
    }

    override fun mostrarError(mensaje: String) {
        mostrarCargando(false)
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun mostrarCargando(mostrar: Boolean) {
        binding.progressCategorias.visibility = if (mostrar) View.VISIBLE else View.GONE
        if (mostrar) {
            binding.tvSinCategorias.visibility = View.GONE
        }
    }
}