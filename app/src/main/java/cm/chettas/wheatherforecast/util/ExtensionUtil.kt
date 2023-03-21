package cm.chettas.wheatherforecast.util

import android.util.Log
import androidx.databinding.library.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

fun dateFormat(value:Date): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(value)
}

fun givenFormat(value:String):Date{
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(value) as Date
}

fun getNextFiveDays():List<Date>{
    val today = Calendar.getInstance() // Today's date
    return  (0..4).map { day ->
        val date = today.clone() as Calendar
        date.add(Calendar.DAY_OF_MONTH, day)
        date.time
    }
}

@Suppress("ControlFlowWithEmptyBody")
inline fun <reified T : Any> T.debug(msg: Any?, tag: String? = null, throwable: Throwable? = null) {
    if (BuildConfig.DEBUG) {
        Log.d(tag ?: this::class.java.simpleName, msg.toString(), throwable)
    } else {
        // Not logging in release mode
    }
}