package com.example.tiendafragments.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiendafragments.data.FavoritesDatabaseHelper
import com.example.tiendafragments.databinding.FragmentFavoritesBinding
import com.example.tiendafragments.ui.adapter.ProductAdapter

class FavoritesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFavoritesBinding.inflate(inflater, container, false).root

    override fun onStart() {
        super.onStart()
        val binding = FragmentFavoritesBinding.bind(requireView())
        val adapter = ProductAdapter()
        binding.recyclerFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavorites.adapter = adapter

        val favorites = FavoritesDatabaseHelper(requireContext()).use { it.getAllProducts() }
        adapter.submitList(favorites)
        binding.textEmpty.visibility = if (favorites.isEmpty()) View.VISIBLE else View.GONE
    }
}
