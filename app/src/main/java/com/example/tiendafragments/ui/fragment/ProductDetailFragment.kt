package com.example.tiendafragments.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.tiendafragments.R
import com.example.tiendafragments.data.FavoritesDatabaseHelper
import com.example.tiendafragments.databinding.FragmentProductDetailBinding
import com.example.tiendafragments.model.Product
import com.google.android.material.snackbar.Snackbar

class ProductDetailFragment : Fragment() {

    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = requireArguments()
        product = Product(
            id = args.getInt(ProductListFragment.ARG_ID),
            title = args.getString(ProductListFragment.ARG_TITLE).orEmpty(),
            price = args.getDouble(ProductListFragment.ARG_PRICE),
            description = args.getString(ProductListFragment.ARG_DESCRIPTION).orEmpty(),
            category = args.getString(ProductListFragment.ARG_CATEGORY).orEmpty(),
            image = args.getString(ProductListFragment.ARG_IMAGE).orEmpty()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentProductDetailBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentProductDetailBinding.bind(view)
        binding.textTitle.text = product.title
        binding.textPrice.text = "%.2f EUR".format(product.price)
        binding.textCategory.text = product.category
        binding.textDescription.text = product.description
        Glide.with(binding.root)
            .load(product.image)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(binding.imageDetail)
        binding.buttonSave.setOnClickListener {
            val saved = FavoritesDatabaseHelper(requireContext()).use { it.saveProduct(product) }
            val message = if (saved) {
                R.string.favorite_saved
            } else {
                R.string.favorite_save_error
            }
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }
    }
}
