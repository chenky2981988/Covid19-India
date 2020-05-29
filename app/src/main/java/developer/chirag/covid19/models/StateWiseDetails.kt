package developer.chirag.covid19.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */

@Parcelize
data class StateWiseDetails(
    val active: String = "0",
    val confirmed: String = "0",
    val deaths: String = "0",
    val recovered: String = "0",
    val state: String = "",

    @Json(name = "deltaconfirmed")
    val deltaConfirmed: String = "",

    @Json(name = "deltadeaths")
    val deltaDeaths: String = "",

    @Json(name = "deltarecovered")
    val deltaRecovered: String = "",

    @Json(name = "lastupdatedtime")
    val lastUpdatedTime: String = "",

    @Json(name = "statecode")
    val stateCode: String = "",

    @Json(name = "statenotes")
    val stateNotes: String = ""

) : Parcelable