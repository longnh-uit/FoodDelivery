package com.longnh.fooddelivery.ui.cartPage

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.longnh.fooddelivery.R
import com.longnh.fooddelivery.controllers.CartController
import com.longnh.fooddelivery.data.repository.CartRepo
import com.longnh.fooddelivery.ui.mainPage.HomeActivity
import com.longnh.fooddelivery.utils.ScreenUtils

class CartActivity : AppCompatActivity() {

    private lateinit var rvCart: RecyclerView
    private lateinit var navBackBtn: FloatingActionButton
    private lateinit var navHomeBtn: FloatingActionButton
    private lateinit var navCartBtn: FloatingActionButton
    private lateinit var cartBtnWrapper: FrameLayout
    private lateinit var btnCheckOut: Button
    private lateinit var tvCost: TextView

    private val controller by lazy {
        CartController(
            CartRepo(
                PreferenceManager.getDefaultSharedPreferences(
                    this
                )
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_cart)

        // Controller

        // Views
        rvCart = findViewById(R.id.rv_cart)
        navBackBtn = findViewById(R.id.nav_back_btn)
        navHomeBtn = findViewById(R.id.nav_home_btn)
        navCartBtn = findViewById(R.id.nav_cart_btn)
        cartBtnWrapper = findViewById(R.id.cart_btn_wrapper)
        tvCost = findViewById(R.id.tv_cost)
        btnCheckOut = findViewById(R.id.btn_check_out)

        rvCart.layoutManager = LinearLayoutManager(this)
        rvCart.adapter = CartAdapter(controller)

        // Initialize badge
        val itemsInCartBadge = BadgeDrawable.create(this)
        itemsInCartBadge.backgroundColor = ContextCompat.getColor(this, R.color.backgroundColor)

        // Listeners
        navBackBtn.setOnClickListener {
            finish()
        }

        navHomeBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        btnCheckOut.setOnClickListener {
            controller.addToHistory()
        }

        controller.update = {
            (rvCart.adapter as CartAdapter).updateDataSet()
            tvCost.text = resources.getString(R.string.total_cost, controller.totalItems())
            setBadge(itemsInCartBadge, cartBtnWrapper, controller.totalItem)
        }

    }

    override fun onResume() {
        super.onResume()
        controller.update?.invoke()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun setBadge(badge: BadgeDrawable, wrapper: FrameLayout, number: Int) {
        badge.horizontalOffset = ScreenUtils.dpToPx(10f).toInt()
        badge.verticalOffset = ScreenUtils.dpToPx(10f).toInt()
        BadgeUtils.attachBadgeDrawable(badge, navCartBtn, wrapper)
        badge.updateBadgeCoordinates(navCartBtn, wrapper)
        badge.number = number
        badge.isVisible = badge.number != 0
        Log.d("badge debug", number.toString())
    }
}