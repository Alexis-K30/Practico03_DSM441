package com.udb.tienda.pb243032.ma243080.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udb.tienda.pb243032.ma243080.databinding.ItemProductoProveedorBinding
import com.udb.tienda.pb243032.ma243080.model.proveedor.ProductoProveedor

// MÉTODO PRINCIPAL: adapta la lista de productos del proveedor para mostrarla en el RecyclerView
class CatalogoAdapter(
    private val productos: List<ProductoProveedor>,
    private val onProductoClick: (ProductoProveedor) -> Unit
) : RecyclerView.Adapter<CatalogoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(val binding: ItemProductoProveedorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoProveedorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        holder.binding.tvNombreProducto.text = producto.title
        holder.binding.tvCategoriaProducto.text = producto.category
        holder.binding.tvPrecioProducto.text = "$${producto.price}"

        Picasso.get()
            .load(producto.thumbnail)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(holder.binding.ivImagenProducto)

        holder.itemView.setOnClickListener {
            onProductoClick(producto)
        }
    }

    override fun getItemCount(): Int = productos.size
}