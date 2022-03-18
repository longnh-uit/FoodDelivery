package com.longnh.fooddelivery.ui.cartPage

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.longnh.fooddelivery.R
import com.longnh.fooddelivery.controllers.CartController
import com.longnh.fooddelivery.controllers.PopularProductController
import com.longnh.fooddelivery.controllers.RecommendedProductController
import com.longnh.fooddelivery.ui.popularFoodPage.PopularFoodDetailActivity
import com.longnh.fooddelivery.ui.recommendedFoodPage.RecommendedFoodDetailActivity
import com.longnh.fooddelivery.utils.BASE_URL
import com.longnh.fooddelivery.utils.UPLOAD_URI

class CartAdapter(private val controller: CartController) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private val dataset = controller.getItems()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodImage: ImageView = view.findViewById(R.id.food_image)
        val cartFoodName: TextView = view.findViewById(R.id.cart_food_name)
        val itemPrice: TextView = view.findViewById(R.id.item_price)
        val tvQuantity: TextView = view.findViewById(R.id.tv_quantity)
        val btnDecreaseQuantity: ImageButton = view.findViewById(R.id.btn_decrease_quantity)
        val btnIncreaseQuantity: ImageButton = view.findViewById(R.id.btn_increase_quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        val holder = ViewHolder(view)
        view.setOnClickListener {
            val popularIndex = PopularProductController
                .popularProductList
                .indexOf(dataset[holder.adapterPosition].product)
            if (popularIndex >= 0) {
                val intent = Intent(view.context, PopularFoodDetailActivity::class.java)
                intent.putExtra("pageId", popularIndex)
                view.context.startActivity(intent)
            } else {
                val recommendedIndex = RecommendedProductController
                    .recommendedProductList
                    .indexOf(dataset[holder.adapterPosition].product)
                val intent = Intent(view.context, RecommendedFoodDetailActivity::class.java)
                intent.putExtra("pageId", recommendedIndex)
                view.context.startActivity(intent)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.foodImage.context).load(BASE_URL + UPLOAD_URI + dataset[position].img).into(holder.foodImage)
        holder.cartFoodName.text = dataset[position].name
        holder.itemPrice.text = holder.itemPrice.context.resources.getString(R.string.item_price, dataset[position].price.toFloat())
        holder.tvQuantity.text = holder.tvQuantity.context.resources.getString(R.string.quantity, dataset[position].quantity)
        holder.btnDecreaseQuantity.setOnClickListener {
            controller.addItem(dataset[position].product, dataset[position].quantity - 1)
        }

        holder.btnIncreaseQuantity.setOnClickListener {
            controller.addItem(dataset[position].product, dataset[position].quantity + 1)
        }
    }

    override fun getItemCount() = dataset.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet() {
        dataset.clear()
        dataset.addAll(controller.getItems())
        notifyDataSetChanged()
    }
}