package com.longnh.fooddelivery.data.api

import com.longnh.fooddelivery.models.Product
import com.longnh.fooddelivery.utils.RECOMMENDED_PRODUCT_URI
import retrofit2.Call
import retrofit2.http.GET

interface RecommendedProductService {
    @GET(RECOMMENDED_PRODUCT_URI)
    suspend fun getRecommendedProductList(): Product
}