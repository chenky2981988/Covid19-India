package developer.chirag.covid19.api

import androidx.annotation.MainThread
import developer.chirag.covid19.api.response.DataResponse
import kotlinx.coroutines.flow.flow
import retrofit2.Response


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */

abstract class NetworkBoundRepository<T> {
    fun asFlow() = flow<DataResponse<T>> {

        // Emit Loading State
        emit(DataResponse.loading())

        try {
            // Fetch latest data from remote
            val covidApiResponse = getDataFromServer()

            // Parse body
            val responseBody = covidApiResponse.body()

            // Check for response validation
            if (covidApiResponse.isSuccessful && responseBody != null) {
                emit(DataResponse.success(responseBody))
            } else {
                // Something went wrong! Emit Error state.
                emit(DataResponse.error(covidApiResponse.message()))
            }
        } catch (e: Exception) {
            // Exception occurred! Emit error
            emit(DataResponse.error("Network error! Please check your internet connection!"))
            e.printStackTrace()
        }
    }

    @MainThread
    protected abstract suspend fun getDataFromServer(): Response<T>
}