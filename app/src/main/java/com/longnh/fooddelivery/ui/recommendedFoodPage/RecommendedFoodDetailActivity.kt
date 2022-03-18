package com.longnh.fooddelivery.ui.recommendedFoodPage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.longnh.fooddelivery.R
import com.longnh.fooddelivery.controllers.RecommendedProductController
import com.longnh.fooddelivery.data.repository.RecommendedProductRepo
import com.longnh.fooddelivery.models.ProductModel
import com.longnh.fooddelivery.ui.cartPage.CartActivity
import com.longnh.fooddelivery.utils.BASE_URL
import com.longnh.fooddelivery.utils.ScreenUtils
import com.longnh.fooddelivery.utils.UPLOAD_URI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecommendedFoodDetailActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var toolbarContainer: FrameLayout
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var foodThumbnail: ImageView
    private lateinit var tvFoodName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvFoodDescription: TextView
    private lateinit var navBackBtn: FloatingActionButton
    private lateinit var navCartBtn: FloatingActionButton
    private lateinit var cartBtnWrapper: FrameLayout
    private lateinit var btnIncreaseQuantity: FloatingActionButton
    private lateinit var btnDecreaseQuantity: FloatingActionButton
    private lateinit var btnAddToCart: MaterialButton

    private lateinit var product: ProductModel
    private var pageId = -1

    private val controller = RecommendedProductController(
        RecommendedProductRepo(
            Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()), this)


    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(R.layout.activity_recommended_food_detail)
        pageId = intent.getIntExtra("pageId", -1)

        // Views
        toolbar = findViewById(R.id.toolbar)
        toolbarContainer = findViewById(R.id.toolbar_container)
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout)
        foodThumbnail = findViewById(R.id.food_thumbnail)
        tvFoodName = findViewById(R.id.tv_food_name)
        tvFoodDescription = findViewById(R.id.tv_food_description)
        tvPrice = findViewById(R.id.tv_price)
        navBackBtn = findViewById(R.id.nav_back_btn)
        navCartBtn = findViewById(R.id.nav_cart_btn)
        cartBtnWrapper = findViewById(R.id.cart_btn_wrapper)
        btnIncreaseQuantity = findViewById(R.id.btn_increase_quantity)
        btnDecreaseQuantity = findViewById(R.id.btn_decrease_quantity)
        btnAddToCart = findViewById(R.id.btn_add_to_cart)

        // init badge
        val itemsInCartBadge = BadgeDrawable.create(this)
        itemsInCartBadge.backgroundColor = ContextCompat.getColor(this, R.color.mainColor)

        product = RecommendedProductController.recommendedProductList[pageId]
        controller.init(product)
        Glide.with(this).load(BASE_URL + UPLOAD_URI + product.img).centerCrop().into(foodThumbnail)
        tvFoodName.text = product.name
        tvFoodDescription.text = product.description
        btnAddToCart.text = resources.getString(R.string.add_to_card, product.price)

        controller.update = {
            tvPrice.text = resources.getString(R.string.price_with_quantity, product.price, controller.quantity)

            // set badge
            itemsInCartBadge.horizontalOffset = ScreenUtils.dpToPx(10f).toInt()
            itemsInCartBadge.verticalOffset = ScreenUtils.dpToPx(10f).toInt()
            BadgeUtils.attachBadgeDrawable(itemsInCartBadge, navCartBtn, cartBtnWrapper)
            itemsInCartBadge.updateBadgeCoordinates(navCartBtn, cartBtnWrapper)
            itemsInCartBadge.number = controller.totalItems
            itemsInCartBadge.isVisible = itemsInCartBadge.number != 0
        }
        controller.update?.invoke()

        ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayout, null)

        setSupportActionBar(toolbar)

        // Listeners
        navBackBtn.setOnClickListener {
            finish()
        }

        navCartBtn.setOnClickListener {
            if (controller.getItems().isNotEmpty()) {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }
        }

        btnDecreaseQuantity.setOnClickListener {
            controller.setQuantity(false)
        }

        btnIncreaseQuantity.setOnClickListener {
            controller.setQuantity(true)
        }

        btnAddToCart.setOnClickListener {
            controller.addItem(product)
        }
    }

    override fun onResume() {
        super.onResume()
        controller.init(product)
        controller.update?.invoke()
    }
}