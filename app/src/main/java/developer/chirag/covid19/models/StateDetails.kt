package developer.chirag.covid19.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */
@Parcelize
data class StateDetails(
    val districtDataList: List<DistrictData>,
    val state: String
): Parcelable