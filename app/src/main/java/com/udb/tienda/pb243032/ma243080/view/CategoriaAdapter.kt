package com.udb.tienda.pb243032.ma243080.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udb.tienda.pb243032.ma243080.databinding.ItemCategoriaBinding
import com.udb.tienda.pb243032.ma243080.model.inventario.Categoria

// MÉTODO PRINCIPAL: adapta la lista de categorías del inventario propio para mostrarla en el RecyclerView
class CategoriaAdapter(
    private val categorias: List<Categoria>,
    private val onCategoriaClick: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    class CategoriaViewHolder(val binding: ItemCategoriaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val binding = ItemCategoriaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoriaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = categorias[position]

        holder.binding.tvNombreCategoriaItem.text = categoria.nombre
        holder.binding.tvDescripcionCategoriaItem.text = categoria.descripcion

        holder.itemView.setOnClickListener {
            onCategoriaClick(categoria)
        }
    }

    override fun getItemCount(): Int = categorias.size
}