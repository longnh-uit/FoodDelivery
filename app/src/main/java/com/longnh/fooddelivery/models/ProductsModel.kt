package com.longnh.fooddelivery.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName(value = "total_size")
    val totalSize: Long,

    @SerializedName(value = "type_id")
    val typeID: Long,

    val offset: Long,
    val products: MutableList<ProductModel>
) {
    companion object {
        fun fromJson(json: String) = Gson().fromJson(json, Product::class.java)
    }
}

data class ProductModel (
    val id: Int,
    val name: String,
    val description: String,
    val price: Long,
    val stars: Float,
    val img: String,
    val location: String,

    @SerializedName(value = "created_at")
    val createdAt: String,

    @SerializedName(value = "updated_at")
    val updatedAt: String,

    @SerializedName(value = "type_id")
    val typeID: Long
) {
    companion object {
        fun fromJson(json: String) = Gson().fromJson(json, ProductModel::class.java)
    }
}
