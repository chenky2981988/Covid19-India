package developer.chirag.covid19.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */
@Parcelize
data class StateWise(
    @Json(name = "statewise")
    val stateWise: List<StateWiseDetails>
) : Parcelable