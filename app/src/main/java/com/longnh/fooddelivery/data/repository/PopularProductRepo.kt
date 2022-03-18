package com.longnh.fooddelivery.data.repository

import com.longnh.fooddelivery.data.api.PopularProductService
import com.longnh.fooddelivery.models.Product
import retrofit2.Call
import retrofit2.Retrofit

class PopularProductRepo(retrofit: Retrofit) {

    private val service = retrofit.create(PopularProductService::class.java)

    suspend fun getPopularProductList() = service.getPopularProductList()
}