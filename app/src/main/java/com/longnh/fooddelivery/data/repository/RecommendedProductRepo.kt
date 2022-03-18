package com.longnh.fooddelivery.data.repository

import com.longnh.fooddelivery.data.api.RecommendedProductService
import com.longnh.fooddelivery.models.Product
import retrofit2.Call
import retrofit2.Retrofit

class RecommendedProductRepo(retrofit: Retrofit) {

    private val service = retrofit.create(RecommendedProductService::class.java)

    suspend fun getRecommendedProductList() = service.getRecommendedProductList()

}