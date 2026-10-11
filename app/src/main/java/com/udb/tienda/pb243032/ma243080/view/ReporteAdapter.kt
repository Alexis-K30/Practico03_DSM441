package com.udb.tienda.pb243032.ma243080.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.udb.tienda.pb243032.ma243080.R
import com.udb.tienda.pb243032.ma243080.databinding.ItemProductoBinding
import com.udb.tienda.pb243032.ma243080.model.inventario.Producto
import java.util.Locale

// MÉTODO PRINCIPAL: adapta la lista de productos con stock bajo para mostrarlos en el RecyclerView del reporte
class ReporteAdapter(
    private val productos: List<Producto>
) : RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder>() {

    class ReporteViewHolder(val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteViewHolder {
        val binding = ItemProductoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ReporteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        val producto = productos[position]

        holder.binding.tvNombreProductoItem.text = producto.nombre
        holder.binding.tvCategoriaProductoItem.text = producto.categoria
        holder.binding.tvPrecioProductoItem.text = String.format(Locale.US, "$%.2f", producto.precio)
        holder.binding.tvStockProductoItem.text = "Stock Bajo: ${producto.stock} un."
        holder.binding.tvStockProductoItem.setTextColor(
            ContextCompat.getColor(holder.itemView.context, R.color.error)
        )
    }

    override fun getItemCount(): Int = productos.size
}