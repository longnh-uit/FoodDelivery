package com.longnh.fooddelivery.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.longnh.fooddelivery.models.CartModel
import com.longnh.fooddelivery.utils.CART_HISTORY_LIST
import com.longnh.fooddelivery.utils.CART_LIST
import org.json.JSONArray
import java.util.*
import kotlin.collections.LinkedHashSet

class CartRepo(private val sharedPreferences: SharedPreferences) {
    companion object {
        private val cart = linkedSetOf<String>()
        private val cartHistory = linkedSetOf<String>()
    }

    fun addToCartList(cartList: MutableList<CartModel>) {
//        sharedPreferences.edit().remove(CART_LIST).apply()
//        sharedPreferences.edit().remove(CART_HISTORY_LIST).apply()
        val time = Date().toString()
        cart.clear()
        for (item in cartList) {
            item.time = time
            cart.add(item.toJson())
        }

        sharedPreferences.edit().putStringSet(CART_LIST, cart).apply()
    }

    fun getCartList(): List<CartModel> {
        var carts = setOf<String>()
        if (sharedPreferences.contains(CART_LIST)) {
            carts = sharedPreferences.getStringSet(CART_LIST, LinkedHashSet<String>())!!
        }
        val cartList = mutableListOf<CartModel>()

        for (item in carts) {
            cartList.add(CartModel.fromJson(item))
        }

        return cartList
    }

    fun getCartHistoryList(): MutableList<CartModel> {
        if (sharedPreferences.contains(CART_HISTORY_LIST)) {
            cartHistory.clear()
            // REASON: putStringSet cannot add item in order
            val jsonArray = JSONArray(sharedPreferences.getString(CART_HISTORY_LIST, "[]"))
            for (i in (0 until jsonArray.length())) {
                cartHistory.add(jsonArray[i].toString())
            }
        }
        val cartHistoryList = mutableListOf<CartModel>()

        for (item in cartHistory)
            cartHistoryList.add(CartModel.fromJson(item))

        return cartHistoryList
    }

    fun addToCartHistoryList() {
        cartHistory.clear()

        if (sharedPreferences.contains(CART_HISTORY_LIST)) {
            val jsonArray = JSONArray(sharedPreferences.getString(CART_HISTORY_LIST, "[]"))
            Log.d("json", "json length: ${jsonArray.length()}")
            for (i in (0 until jsonArray.length())) {
                cartHistory.add(jsonArray[i].toString())
            }
            Log.d("cart", "cart length after add: ${cartHistory.size}")
        }

        cartHistory.addAll(cart)
        removeCart()
        // REASON: putStringSet cannot add item in order
        val jsonArray = JSONArray(cartHistory).toString()
        sharedPreferences.edit().putString(CART_HISTORY_LIST, jsonArray).apply()
        Log.d("func check", "cart history length ${getCartHistoryList().size}")
    }

    private fun removeCart() {
        cart.clear()
        sharedPreferences.edit().remove(CART_LIST).apply()
    }
}