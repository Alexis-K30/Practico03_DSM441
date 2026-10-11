package com.udb.tienda.pb243032.ma243080.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udb.tienda.pb243032.ma243080.databinding.ItemProductoBinding
import com.udb.tienda.pb243032.ma243080.model.inventario.Producto

// MÉTODO PRINCIPAL: adapta la lista de productos del inventario propio para mostrarla en el RecyclerView
class ProductoAdapter(
    private val productos: List<Producto>,
    private val onProductoClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        holder.binding.tvNombreProductoItem.text = producto.nombre
        holder.binding.tvCategoriaProductoItem.text = "ID: ${producto.id} • ${producto.categoria}"
        holder.binding.tvPrecioProductoItem.text = "$${producto.precio}"
        holder.binding.tvStockProductoItem.text = "Stock: ${producto.stock}"

        holder.itemView.setOnClickListener {
            onProductoClick(producto)
        }
    }

    override fun getItemCount(): Int = productos.size
}