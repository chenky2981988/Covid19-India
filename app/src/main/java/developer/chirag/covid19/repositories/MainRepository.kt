package developer.chirag.covid19.repositories

import developer.chirag.covid19.api.Covid19IndiaApiService
import developer.chirag.covid19.api.NetworkBoundRepository
import developer.chirag.covid19.api.response.DataResponse
import developer.chirag.covid19.models.StateDetails
import developer.chirag.covid19.models.StateWise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */
@FlowPreview
@ExperimentalCoroutinesApi
class MainRepository(private val covidApiService: Covid19IndiaApiService) {

    fun getCovidIndiaData(): Flow<DataResponse<StateWise>> {
        return object : NetworkBoundRepository<StateWise>() {
            override suspend fun getDataFromServer(): Response<StateWise> =
                covidApiService.getCovid19Data()
        }.asFlow().flowOn(Dispatchers.IO)
    }

    fun getStateDistrictWiseData(stateName: String): Flow<DataResponse<StateDetails>> {
        return object : NetworkBoundRepository<List<StateDetails>>() {
            override suspend fun getDataFromServer(): Response<List<StateDetails>> =
                covidApiService.getStateDistrictWiseData()
        }.asFlow().flowOn(Dispatchers.IO).map { response ->
            when (response) {
                is DataResponse.Loading -> DataResponse.loading<StateDetails>()
                is DataResponse.Success -> {
                    val stateData = response.data.find {
                        it.state == stateName }

                    if (stateData != null) {
                        DataResponse.success<StateDetails>(stateData)
                    } else {
                        DataResponse.error<StateDetails>("No data found for State '$stateName'")
                    }
                }
                is DataResponse.Error -> DataResponse.error<StateDetails>(response.message)
            }

        }
    }
}