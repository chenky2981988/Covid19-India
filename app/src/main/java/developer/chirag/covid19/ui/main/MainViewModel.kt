package developer.chirag.covid19.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import developer.chirag.covid19.api.response.DataResponse
import developer.chirag.covid19.models.StateWise
import developer.chirag.covid19.repositories.MainRepositoy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */
@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivityViewModel(private val mainRepositoy: MainRepositoy) : ViewModel() {
    private val covid19MutableLiveData = MutableLiveData<DataResponse<StateWise>>()

    val covid19LiveData: LiveData<DataResponse<StateWise>> = covid19MutableLiveData

    fun getIndiaCovid19Data() {
        viewModelScope.launch {
            mainRepositoy.getCovidIndiaData().collect {
                covid19MutableLiveData.value = it
            }
        }
    }


}