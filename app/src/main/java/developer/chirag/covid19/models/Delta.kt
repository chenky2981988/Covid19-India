package developer.chirag.covid19.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */
@Parcelize
data class Delta(val confirmed: Int = 0, val deceased: Int = 0, val recovered: Int = 0) : Parcelable