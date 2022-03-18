package com.longnh.fooddelivery.ui.popularFoodPage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.longnh.fooddelivery.R
import com.longnh.fooddelivery.controllers.PopularProductController
import com.longnh.fooddelivery.data.repository.PopularProductRepo
import com.longnh.fooddelivery.models.ProductModel
import com.longnh.fooddelivery.ui.cartPage.CartActivity
import com.longnh.fooddelivery.utils.BASE_URL
import com.longnh.fooddelivery.utils.ScreenUtils
import com.longnh.fooddelivery.utils.UPLOAD_URI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.ceil

class PopularFoodDetailActivity: AppCompatActivity() {

    private lateinit var image: ImageView
    private lateinit var popularFoodName: TextView
    private lateinit var foodRating: RatingBar
    private lateinit var tvFoodRating: TextView
    private lateinit var tvIntroduce: TextView
    private lateinit var btnShowmore: CardView
    private lateinit var btnShowmoreTitle: TextView
    private lateinit var btnShowmoreIcon: ImageView
    private lateinit var container: LinearLayout
    private lateinit var navBackBtn: FloatingActionButton
    private lateinit var navCartBtn: FloatingActionButton
    private lateinit var btnAddToCart: MaterialButton
    private lateinit var btnDecreaseQuantity: ImageButton
    private lateinit var btnIncreaseQuantity: ImageButton
    private lateinit var tvQuantity: TextView
    private lateinit var cartBtnWrapper: FrameLayout

    private lateinit var product: ProductModel
    private var pageId = -1

    // delete later
    private val controller = PopularProductController(PopularProductRepo(Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()), this@PopularFoodDetailActivity)

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_popular_food_detail)
        pageId = intent.getIntExtra("pageId", -1)

        // Views
        image = findViewById(R.id.image)
        popularFoodName = findViewById(R.id.popular_food_name)
        foodRating = findViewById(R.id.food_rating)
        tvFoodRating = findViewById(R.id.tv_food_rating)
        tvIntroduce = findViewById(R.id.tv_introduce)
        btnShowmore = findViewById(R.id.btn_showmore)
        btnShowmoreTitle = findViewById(R.id.btn_showmore_title)
        btnShowmoreIcon = findViewById(R.id.btn_showmore_icon)
        container = findViewById(R.id.container)
        navBackBtn = findViewById(R.id.nav_back_btn)
        navCartBtn = findViewById(R.id.nav_cart_btn)
        btnAddToCart = findViewById(R.id.btn_add_to_cart)
        btnDecreaseQuantity = findViewById(R.id.btn_decrease_quantity)
        btnIncreaseQuantity = findViewById(R.id.btn_increase_quantity)
        tvQuantity = findViewById(R.id.tv_quantity)
        cartBtnWrapper = findViewById(R.id.cart_btn_wrapper)

        // init badge
        val itemsInCartBadge = BadgeDrawable.create(this)
        itemsInCartBadge.backgroundColor = ContextCompat.getColor(this, R.color.mainColor)

        // First run only
        product = PopularProductController.popularProductList[pageId]
        controller.init(product)
        Glide.with(this).load(BASE_URL + UPLOAD_URI + product.img).centerCrop().into(image)
        popularFoodName.text = product.name
        foodRating.rating = product.stars
        tvFoodRating.text = product.stars.toString()
        tvIntroduce.text = product.description
        if (!tvIntroduce.isEllipsized()) {
            btnShowmore.visibility = View.GONE
        }
        // end of fist run

        controller.update = {
            btnAddToCart.text = resources.getString(R.string.add_to_card, product.price)
            tvQuantity.text = resources.getString(R.string.quantity, controller.quantity)

            // set badge
            itemsInCartBadge.horizontalOffset = ScreenUtils.dpToPx(10f).toInt()
            itemsInCartBadge.verticalOffset = ScreenUtils.dpToPx(10f).toInt()
            BadgeUtils.attachBadgeDrawable(itemsInCartBadge, navCartBtn, cartBtnWrapper)
            itemsInCartBadge.updateBadgeCoordinates(navCartBtn, cartBtnWrapper)
            itemsInCartBadge.number = controller.totalItems
            Log.d("delete", "${controller.totalItems}")
            itemsInCartBadge.isVisible = itemsInCartBadge.number != 0
        }

        // Listeners
        btnShowmore.setOnClickListener {
            if (tvIntroduce.maxLines != 5) {
                btnShowmoreTitle.text = resources.getText(R.string.show_more)
                btnShowmoreIcon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                tvIntroduce.maxLines = 5
            }
            else {
                btnShowmoreTitle.text = resources.getText(R.string.show_less)
                btnShowmoreIcon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                tvIntroduce.maxLines = Integer.MAX_VALUE
            }
        }

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

    private fun TextView.isEllipsized(): Boolean {
        val textPixelLength = paint.measureText(text.toString())
        val numberOfLines = ceil((textPixelLength / measuredWidth).toDouble())
        return lineHeight * numberOfLines > measuredHeight
    }
}