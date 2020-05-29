package developer.chirag.covid19.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */

@SuppressLint("SimpleDateFormat")
fun getLastUpdatedDisplay(lastUpdated: String): String {
    val currentTime = Date()
    val lastUpdatedTime = lastUpdated.getInDateFormat()
    val seconds = TimeUnit.MILLISECONDS.toSeconds(currentTime.time - lastUpdatedTime.time)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime.time - lastUpdatedTime.time)
    val hours = TimeUnit.MILLISECONDS.toHours(currentTime.time - lastUpdatedTime.time)

    if(seconds < 60) {
        return "$seconds seconds ago"
    } else if(minutes < 60) {
        return "$minutes minutes ago"
    } else if(hours < 24) {
        return "$hours hour ${minutes % 60} minutes ago"
    } else {
        return SimpleDateFormat("dd/MM/yy, hh:mm a").format(lastUpdatedTime).toString()
    }

}

//Extended Function
@SuppressLint("SimpleDateFormat")
fun String.getInDateFormat(): Date {
    return SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(this)
}