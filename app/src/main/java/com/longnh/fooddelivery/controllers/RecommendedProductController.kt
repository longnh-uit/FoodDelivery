package com.longnh.fooddelivery.controllers

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.longnh.fooddelivery.data.repository.CartRepo
import com.longnh.fooddelivery.data.repository.RecommendedProductRepo
import com.longnh.fooddelivery.models.CartModel
import com.longnh.fooddelivery.models.Product
import com.longnh.fooddelivery.models.ProductModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendedProductController(private val recommendedProductRepo: RecommendedProductRepo, private val context: Context) {
    companion object {
        val recommendedProductList: MutableList<ProductModel> = mutableListOf()
    }
    var quantity = 0
        private set
    var totalItems = 0
        private set
        get() = cart.totalItem

    private val cart by lazy { CartController(CartRepo(PreferenceManager.getDefaultSharedPreferences(context))) }
    var update: (() -> Unit)? = null
    var init: (() -> Unit)? = null

    fun init(product: ProductModel) {
        quantity = if (cart.existInCart(product)) {
            cart.getQuantity(product)
        } else {
            0
        }
    }

    suspend fun getRecommendedProductList() {
        try {
            val response = recommendedProductRepo.getRecommendedProductList()
            recommendedProductList.clear()
            recommendedProductList.addAll(response.products)
            if (init != null) {
                init!!.invoke()
                init = null
            }
            update?.invoke()

        } catch (e: Throwable) {
            Log.d("Recommended Data", "something wrong: ${e.message}")
        }

    }

    fun setQuantity(isIncrement: Boolean) {
        quantity = if (isIncrement) {
            checkQuantity(quantity + 1)
        } else {
            checkQuantity(quantity - 1)
        }
        update?.invoke()
    }

    private fun checkQuantity(quantity: Int): Int {
        return when {
            quantity < 0 -> {
                Toast.makeText(context, "You can't reduce more!", Toast.LENGTH_SHORT).show()
                0
            }
            quantity > 20 -> {
                Toast.makeText(context, "You can't add more!", Toast.LENGTH_SHORT).show()
                20
            }
            else -> quantity
        }
    }

    fun addItem(product: ProductModel) {
        if (quantity == 0) {
            Toast.makeText(context, "You should at least add an item in the cart!", Toast.LENGTH_SHORT).show()
        }
        cart.addItem(product, quantity)
        update?.invoke()
        for (item in CartController.items) {
            Log.d("cart item", "The id is ${item.value} and quantity is ${item.value.quantity}")

        }
    }

    fun getItems(): List<CartModel> = cart.getItems()
}