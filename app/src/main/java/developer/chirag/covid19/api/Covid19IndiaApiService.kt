package developer.chirag.covid19.api

import developer.chirag.covid19.models.StateDetails
import developer.chirag.covid19.models.StateWise
import retrofit2.Response
import retrofit2.http.GET


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */
interface Covid19IndiaApiService {

    @GET("data.json")
    suspend fun getCovid19Data(): Response<StateWise>

    @GET("v2/state_district_wise.json")
    suspend fun getStateDistrictWiseData(): Response<List<StateDetails>>

    companion object {
        const val BASE_URL = "https://api.covid19india.org/"
    }
}