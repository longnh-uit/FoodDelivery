package com.longnh.fooddelivery.ui.mainPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.longnh.fooddelivery.R
import com.longnh.fooddelivery.ui.cartPage.CartHistoryFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_home)

        // Views
        bottomNav = findViewById(R.id.bottom_nav)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().replace(R.id.container, MainFoodPage()).commit()

        // Listeners
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> supportFragmentManager.beginTransaction().replace(R.id.container, MainFoodPage()).commit()
                R.id.action_cart -> supportFragmentManager.beginTransaction().replace(R.id.container, CartHistoryFragment()).commit()
            }
            true
        }

    }
}