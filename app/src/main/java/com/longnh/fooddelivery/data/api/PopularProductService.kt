package com.longnh.fooddelivery.data.api

import com.longnh.fooddelivery.models.Product
import com.longnh.fooddelivery.utils.POPULAR_PRODUCT_URI
import retrofit2.http.GET

interface PopularProductService {
    @GET(POPULAR_PRODUCT_URI)
    suspend fun getPopularProductList(): Product
}