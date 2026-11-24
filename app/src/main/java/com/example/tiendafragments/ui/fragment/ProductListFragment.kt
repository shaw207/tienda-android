package com.example.tiendafragments.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import org.json.JSONException
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiendafragments.R
import com.example.tiendafragments.data.ProductRepository
import com.example.tiendafragments.databinding.FragmentProductListBinding
import com.example.tiendafragments.model.Product
import com.example.tiendafragments.ui.adapter.ProductAdapter

class ProductListFragment : Fragment() {

    private var productList: List<Product>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentProductListBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentProductListBinding.bind(view)
        val adapter = ProductAdapter(onProductClick = ::openDetail)
        binding.recyclerProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerProducts.adapter = adapter
        binding.buttonFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_productListFragment_to_favoritesFragment)
        }

        val repository = ProductRepository(requireContext().applicationContext)
        binding.progressProducts.visibility = View.VISIBLE
        binding.textMessage.visibility = View.GONE
        viewLifecycleOwner.lifecycleScope.launch {
            val products = productList ?: withContext(Dispatchers.IO) {
                try {
                    repository.getProducts()
                } catch (_: IOException) {
                    null
                } catch (_: JSONException) {
                    null
                }
            }
            binding.progressProducts.visibility = View.GONE
            if (products == null) {
                binding.textMessage.setText(R.string.catalog_error)
                binding.textMessage.visibility = View.VISIBLE
            } else {
                productList = products
                adapter.submitList(products)
            }
        }
    }

    private fun openDetail(product: Product) {
        val bundle = Bundle().apply {
            putInt(ARG_ID, product.id)
            putString(ARG_TITLE, product.title)
            putDouble(ARG_PRICE, product.price)
            putString(ARG_DESCRIPTION, product.description)
            putString(ARG_CATEGORY, product.category)
            putString(ARG_IMAGE, product.image)
        }
        findNavController().navigate(R.id.action_productListFragment_to_productDetailFragment, bundle)
    }

    companion object {
        const val ARG_ID = "id"
        const val ARG_TITLE = "title"
        const val ARG_PRICE = "price"
        const val ARG_DESCRIPTION = "description"
        const val ARG_CATEGORY = "category"
        const val ARG_IMAGE = "image"
    }
}
