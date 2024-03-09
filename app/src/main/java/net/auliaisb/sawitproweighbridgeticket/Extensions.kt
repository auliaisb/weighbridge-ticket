package net.auliaisb.sawitproweighbridgeticket

import android.content.Context
import android.util.TypedValue

object Extensions {
    fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

    fun Int.dpToPx(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}