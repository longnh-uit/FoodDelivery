package com.longnh.fooddelivery.ui.cartPage

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.longnh.fooddelivery.R
import com.longnh.fooddelivery.controllers.CartController
import com.longnh.fooddelivery.data.repository.CartRepo


class CartHistoryFragment : Fragment(R.layout.fragment_cart_history) {

    private lateinit var toolbar: FrameLayout
    private lateinit var rvCartHistory: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Views
        toolbar = view.findViewById(R.id.toolbar)
        rvCartHistory = view.findViewById(R.id.rv_cart_history)

        // controller
        val controller = CartController(CartRepo(PreferenceManager.getDefaultSharedPreferences(requireContext())))

        // set toolbar inset
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { it, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())

            it.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }

            WindowInsetsCompat.CONSUMED
        }

        // setup recycler view
        rvCartHistory.adapter = CartHistoryAdapter(controller)
    }
}

