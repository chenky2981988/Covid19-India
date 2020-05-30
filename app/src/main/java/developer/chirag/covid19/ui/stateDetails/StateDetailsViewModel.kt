package developer.chirag.covid19.ui.stateDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import developer.chirag.covid19.api.response.DataResponse
import developer.chirag.covid19.models.StateDetails
import developer.chirag.covid19.repositories.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * Created by Chirag Sidhiwala on 30/5/20.
 */
@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class StateDetailsViewModel(private val mainRepository: MainRepository): ViewModel() {
    private val stateCovid19MutableLiveData = MutableLiveData<DataResponse<StateDetails>>()
    val stateCovid19LiveData: LiveData<DataResponse<StateDetails>> = stateCovid19MutableLiveData

    fun getStateDistrictReport(stateName: String) {
        viewModelScope.launch {
            mainRepository.getStateDistrictWiseData(stateName).collect {
                stateCovid19MutableLiveData.value = it
            }
        }
    }
}