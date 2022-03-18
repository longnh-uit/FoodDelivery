package com.longnh.fooddelivery.ui.mainPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.longnh.fooddelivery.R
import com.longnh.fooddelivery.controllers.PopularProductController
import com.longnh.fooddelivery.controllers.RecommendedProductController
import com.longnh.fooddelivery.data.repository.PopularProductRepo
import com.longnh.fooddelivery.data.repository.RecommendedProductRepo
import com.longnh.fooddelivery.utils.BASE_URL
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainFoodPage : Fragment(R.layout.page_main_food) {

    private lateinit var vpPopularFood: ViewPager2
    private lateinit var indicator: DotsIndicator
    private lateinit var rvRecommendedFood: RecyclerView
    private lateinit var popularFoodProgressCircular: CircularProgressIndicator
    private lateinit var recommendedFoodProgressCircular: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mActivity = requireActivity()

        // Views
        vpPopularFood = view.findViewById(R.id.vp_food)
        indicator = view.findViewById(R.id.indicator)
        rvRecommendedFood = view.findViewById(R.id.rv_popular_food)
        popularFoodProgressCircular = view.findViewById(R.id.popular_food_progress_circular)
        recommendedFoodProgressCircular = view.findViewById(R.id.recommended_food_progress_circular)

        // update popular
        vpPopularFood.adapter = PopularFoodAdaper()
        indicator.setViewPager2(vpPopularFood)
        popularFoodProgressCircular.visibility = View.GONE
        vpPopularFood.visibility = View.VISIBLE
        indicator.visibility = View.VISIBLE



        rvRecommendedFood.layoutManager = LinearLayoutManager(mActivity)
        rvRecommendedFood.adapter = RecommendedFoodAdapter()
        rvRecommendedFood.visibility = View.VISIBLE
        recommendedFoodProgressCircular.visibility = View.GONE


        vpPopularFood.offscreenPageLimit = 1
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
            // If you want a fading effect uncomment the next line:
            page.alpha = 0.25f + (1 - kotlin.math.abs(position))
        }

        // The ItemDecoration gives the current (centered) item horizontal margin so that
        // it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            requireActivity().applicationContext!!,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        vpPopularFood.addItemDecoration(itemDecoration)
        vpPopularFood.setPageTransformer(pageTransformer)

    }
}