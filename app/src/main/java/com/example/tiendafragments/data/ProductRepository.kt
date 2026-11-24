package com.example.tiendafragments.data

import android.content.Context
import com.example.tiendafragments.model.Product
import org.json.JSONObject

class ProductRepository(private val context: Context) {
    fun getProducts(): List<Product> {
        val catalog = context.assets.open("catalog/products.json").bufferedReader().use { it.readText() }
        val products = JSONObject(catalog).getJSONArray("products")
        return List(products.length()) { index ->
            val item = products.getJSONObject(index)
            Product(
                id = item.getInt("id"),
                title = item.getString("title"),
                price = item.getDouble("price"),
                description = item.getString("description"),
                category = item.getString("category"),
                image = item.getString("thumbnail")
            )
        }
    }
}
