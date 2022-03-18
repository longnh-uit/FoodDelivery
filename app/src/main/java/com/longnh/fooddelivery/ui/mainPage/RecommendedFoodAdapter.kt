package com.longnh.fooddelivery.ui.mainPage

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.longnh.fooddelivery.R
import com.longnh.fooddelivery.ui.recommendedFoodPage.RecommendedFoodDetailActivity
import com.longnh.fooddelivery.controllers.RecommendedProductController
import com.longnh.fooddelivery.utils.BASE_URL
import com.longnh.fooddelivery.utils.UPLOAD_URI

class RecommendedFoodAdapter : RecyclerView.Adapter<RecommendedFoodAdapter.MyViewHolder>() {

    private val dataset = RecommendedProductController.recommendedProductList

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recommendedFoodName = view.findViewById<TextView>(R.id.recommended_food_name)
        val foodDescription = view.findViewById<TextView>(R.id.food_description)
        val foodImage = view.findViewById<ImageView>(R.id.food_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_recommended, parent, false)
        val holder = MyViewHolder(view)
        view.setOnClickListener {
            val intent = Intent(view.context, RecommendedFoodDetailActivity::class.java)
            intent.putExtra("pageId", holder.adapterPosition)
            view.context.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.recommendedFoodName.text = dataset[position].name
        holder.foodDescription.text = dataset[position].description
        Glide.with(holder.foodImage.context).load(BASE_URL + UPLOAD_URI + dataset[position].img).into(holder.foodImage)
    }

    override fun getItemCount() = dataset.size

}