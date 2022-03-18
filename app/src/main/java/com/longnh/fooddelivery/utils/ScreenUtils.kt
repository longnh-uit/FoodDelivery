package com.longnh.fooddelivery.utils

import android.content.res.Resources
import android.util.TypedValue

class ScreenUtils {
    companion object {
        fun dpToPx(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics)
    }
}