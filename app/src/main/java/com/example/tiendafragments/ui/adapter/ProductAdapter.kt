package com.example.tiendafragments.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiendafragments.R
import com.example.tiendafragments.databinding.ItemProductBinding
import com.example.tiendafragments.model.Product

class ProductAdapter(
    private var products: List<Product> = emptyList(),
    private val onProductClick: ((Product) -> Unit)? = null
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    fun submitList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.textTitle.text = product.title
            binding.textCategory.text = product.category
            binding.textPrice.text = "%.2f EUR".format(product.price)
            Glide.with(binding.root)
                .load(product.image)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(binding.imageProduct)
            onProductClick?.let { openProduct ->
                binding.root.setOnClickListener { openProduct(product) }
            }
        }
    }
}
