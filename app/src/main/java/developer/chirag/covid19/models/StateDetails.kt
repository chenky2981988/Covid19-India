package developer.chirag.covid19.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class StateDetails(
    val districtData: List<DistrictData>,
    val state: String
): Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class DistrictData(
    val notes: String = "",
    val active: Int = 0,
    val confirmed: Int = 0,
    val deceased: Int = 0,
    val recovered: Int = 0,
    val delta: Delta,
    val district: String = ""
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Delta(val confirmed: Int = 0, val deceased: Int = 0, val recovered: Int = 0) : Parcelable