package developer.chirag.covid19.di

import developer.chirag.covid19.ui.main.MainActivityViewModel
import developer.chirag.covid19.ui.stateDetails.StateDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */
@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
val viewModelModule = module {
    viewModel { MainActivityViewModel(get()) }
    viewModel { StateDetailsViewModel(get()) }
}