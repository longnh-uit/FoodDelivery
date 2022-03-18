package com.longnh.fooddelivery.models

import com.google.gson.Gson

data class CartModel(
    val id: Int,
    val name: String,
    val price: Long,
    val img: String,
    var quantity: Int,
    val isExist: Boolean,
    var time: String,
    val product: ProductModel
) {
    companion object {
        fun fromJson(json: String): CartModel = Gson().fromJson(json, CartModel::class.java)
    }

    fun toJson(): String = Gson().toJson(this)
}
