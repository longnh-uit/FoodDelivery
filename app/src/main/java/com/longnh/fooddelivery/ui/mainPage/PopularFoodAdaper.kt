package com.longnh.fooddelivery.ui.mainPage

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.longnh.fooddelivery.ui.popularFoodPage.PopularFoodDetailActivity
import com.longnh.fooddelivery.R
import com.longnh.fooddelivery.controllers.PopularProductController
import com.longnh.fooddelivery.utils.BASE_URL
import com.longnh.fooddelivery.utils.UPLOAD_URI

class PopularFoodAdaper : RecyclerView.Adapter<PopularFoodAdaper.ViewHolder>() {

    private val dataset = PopularProductController.popularProductList

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagePopularFood: ImageView = view.findViewById(R.id.image_popular_food)
        val tvPopularFoodName: TextView = view.findViewById(R.id.popular_food_name)
        val foodRatingBar: RatingBar = view.findViewById(R.id.food_rating)
        val tvFoodRating: TextView = view.findViewById(R.id.tv_food_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_popular, parent, false)
        val holder = ViewHolder(view)
        view.setOnClickListener {
            val intent = Intent(view.context, PopularFoodDetailActivity::class.java)
            intent.putExtra("pageId", holder.adapterPosition)
            view.context.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.imagePopularFood.context).load(BASE_URL + UPLOAD_URI + dataset[position].img).into(holder.imagePopularFood)
        holder.tvPopularFoodName.text = dataset[position].name
        holder.foodRatingBar.rating = dataset[position].stars
        holder.tvFoodRating.text = dataset[position].stars.toString()
    }

    override fun getItemCount() = dataset.size

}