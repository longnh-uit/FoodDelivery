package com.longnh.fooddelivery.ui.cartPage

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.longnh.fooddelivery.R
import com.longnh.fooddelivery.controllers.CartController
import com.longnh.fooddelivery.utils.BASE_URL
import com.longnh.fooddelivery.utils.ScreenUtils
import com.longnh.fooddelivery.utils.UPLOAD_URI
import java.lang.Integer.parseInt
import java.lang.Long.parseLong
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.LinkedHashMap

class CartHistoryAdapter(controller: CartController): RecyclerView.Adapter<CartHistoryAdapter.ViewHolder>() {

    private val getCartHistoryList = controller.getCartHistoryList().asReversed()
    private val cartItemPerOrder = LinkedHashMap<String, Pair<Int, Int>>()
    private val orderTimes: List<Pair<Int, Int>>

    init {
        for ((startPos, item) in getCartHistoryList.withIndex()) {
            if (cartItemPerOrder.containsKey(item.time)) {
                cartItemPerOrder[item.time] = cartItemPerOrder[item.time]!!.copy(second = cartItemPerOrder[item.time]!!.second + 1)
            }
            else cartItemPerOrder[item.time] = Pair(startPos, 1)
        }

        orderTimes = cartItemPerOrder.entries.map {
            it.value
        }.toList()

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val thumbnailContainer: LinearLayout = view.findViewById(R.id.thumbnail_container)
        val tvNumOfItem: TextView = view.findViewById(R.id.tv_num_of_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.thumbnailContainer.context
        val dateFormat = SimpleDateFormat("E MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH)
        val date = dateFormat.parse(getCartHistoryList[orderTimes[position].first].time)
        val parseDate = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH).format(date!!)

        holder.tvDate.text = parseDate
        holder.tvNumOfItem.text = context.resources.getString(R.string.items, orderTimes[position].second)
        for (i in (0 until orderTimes[position].second)) {
            val imageView = ImageView(context)
            val cardView = CardView(context)
            val cardMargin = ScreenUtils.dpToPx(2.5f).toInt()
            cardView.layoutParams = LinearLayout.LayoutParams(ScreenUtils.dpToPx(70f).toInt(), ScreenUtils.dpToPx(70f).toInt())
            (cardView.layoutParams as LinearLayout.LayoutParams).setMargins(cardMargin, 0, cardMargin, 0)
            cardView.cardElevation = 1f
            cardView.radius = ScreenUtils.dpToPx(7.5f)
            imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            cardView.addView(imageView)
            val index = orderTimes[position].first + i
            Glide.with(context).load(BASE_URL + UPLOAD_URI + getCartHistoryList[index].img).centerCrop().into(imageView)
            holder.thumbnailContainer.addView(cardView)
            if (holder.thumbnailContainer.childCount >= 3) break
        }
    }

    override fun getItemCount() = orderTimes.size

}