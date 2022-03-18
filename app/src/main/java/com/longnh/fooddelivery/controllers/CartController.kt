package com.longnh.fooddelivery.controllers

import com.longnh.fooddelivery.data.repository.CartRepo
import com.longnh.fooddelivery.models.CartModel
import com.longnh.fooddelivery.models.ProductModel
import java.util.*

class CartController(private val cartRepo: CartRepo) {

    companion object {
        val items = mutableMapOf<Int, CartModel>()
    }
    var totalItem: Int = 0
        private set
        get() {
            var totalQuantity = 0
            for (item in items) {
                totalQuantity += item.value.quantity
            }
            return totalQuantity
        }
    private val storageItems = mutableListOf<CartModel>()

    var update: (() -> Unit)? = null

    fun addItem(product: ProductModel, quantity: Int) {

        items[product.id] = CartModel(
            product.id,
            product.name,
            product.price,
            product.img,
            quantity,
            true,
            Calendar.getInstance().time.toString(),
            product
        )

        if (items[product.id]!!.quantity == 0)
            items.remove(product.id)

        cartRepo.addToCartList(getItems())
        update?.invoke()
    }

    fun existInCart(product: ProductModel): Boolean {
        return items.containsKey(product.id)
    }

    fun getQuantity(product: ProductModel): Int {
        var quantity = 0
        if (items.containsKey(product.id)) {
            quantity = items[product.id]!!.quantity
        }
        return quantity
    }

    fun getItems() = items.entries.map { item -> item.value }.toMutableList()
    fun totalItems(): Long {
        var total: Long = 0
        for (item in items) {
            total += item.value.quantity * item.value.price
        }
        return total
    }

    fun getCartData(): MutableList<CartModel> {
        storageItems.clear()
        storageItems.addAll(cartRepo.getCartList())
        for (item in storageItems) {
            items[item.product.id] = item
        }

        return storageItems
    }

    fun addToHistory() {
        cartRepo.addToCartHistoryList()
        clear()
    }

    private fun clear() {
        items.clear()
        update?.invoke()
    }

    fun getCartHistoryList() = cartRepo.getCartHistoryList()
}