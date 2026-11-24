package com.example.tiendafragments.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tiendafragments.model.Product

class FavoritesDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_FAVORITES (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_PRICE REAL NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_CATEGORY TEXT NOT NULL,
                $COLUMN_IMAGE TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        throw android.database.sqlite.SQLiteException("No migration from $oldVersion to $newVersion")
    }

    fun saveProduct(product: Product): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_ID, product.id)
            put(COLUMN_TITLE, product.title)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_DESCRIPTION, product.description)
            put(COLUMN_CATEGORY, product.category)
            put(COLUMN_IMAGE, product.image)
        }

        val result = writableDatabase.insertWithOnConflict(
            TABLE_FAVORITES,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        return result != -1L
    }

    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val cursor = readableDatabase.query(
            TABLE_FAVORITES,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_TITLE ASC"
        )

        cursor.use {
            while (it.moveToNext()) {
                products.add(
                    Product(
                        id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                        price = it.getDouble(it.getColumnIndexOrThrow(COLUMN_PRICE)),
                        description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        category = it.getString(it.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        image = it.getString(it.getColumnIndexOrThrow(COLUMN_IMAGE))
                    )
                )
            }
        }

        return products
    }

    companion object {
        private const val DATABASE_NAME = "tienda.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_FAVORITES = "favorites"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_IMAGE = "image"
    }
}
