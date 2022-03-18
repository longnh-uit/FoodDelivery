package com.longnh.fooddelivery.ui.splashPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.longnh.fooddelivery.R
import com.longnh.fooddelivery.controllers.CartController
import com.longnh.fooddelivery.controllers.PopularProductController
import com.longnh.fooddelivery.controllers.RecommendedProductController
import com.longnh.fooddelivery.data.repository.CartRepo
import com.longnh.fooddelivery.data.repository.PopularProductRepo
import com.longnh.fooddelivery.data.repository.RecommendedProductRepo
import com.longnh.fooddelivery.ui.mainPage.HomeActivity
import com.longnh.fooddelivery.utils.BASE_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SplashActivity : AppCompatActivity() {

    private lateinit var ivLogo1: ImageView
    private lateinit var ivLogo2: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        CartController(CartRepo(PreferenceManager.getDefaultSharedPreferences(this))).getCartData()
        CoroutineScope(IO).launch {
            PopularProductController(PopularProductRepo(retrofit), this@SplashActivity).getPopularProductList()
            RecommendedProductController(RecommendedProductRepo(retrofit), this@SplashActivity).getRecommendedProductList()
        }

        setContentView(R.layout.activity_splash)

        // Views
        ivLogo1 = findViewById(R.id.iv_logo_1)
        ivLogo2 = findViewById(R.id.iv_logo_2)

        // Animation
        val zoom = AnimationUtils.loadAnimation(applicationContext, R.anim.spash_anim)
        ivLogo1.startAnimation(zoom)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}