package utils.constant

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

fun parseEventDate(inputDate: String, format: String): Date? {
    val sdf = SimpleDateFormat(format)
    return sdf.parse(inputDate)
}

fun showToast(context: Context, message: String, period: Int) {
    Toast.makeText(context, message, period).show()
}



