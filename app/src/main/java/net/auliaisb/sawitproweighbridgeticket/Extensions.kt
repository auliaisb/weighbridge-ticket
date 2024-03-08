package net.auliaisb.sawitproweighbridgeticket

import android.content.Context
import android.util.TypedValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object Extensions {
    fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()
    inline fun <reified T> DatabaseReference.asFlow(): Flow<List<T>> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.children.mapNotNull { it.getValue(T::class.java) }
                trySend(result).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        addValueEventListener(valueEventListener)
        awaitClose { removeEventListener(valueEventListener) }
    }

    fun Int.dpToPx(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}