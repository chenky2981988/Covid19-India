package developer.chirag.covid19

import android.app.Application
import developer.chirag.covid19.di.retrofitModule
import developer.chirag.covid19.di.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */
@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class Covid19UpdateApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin()
    }

    private fun initializeKoin() {
        startKoin {
            androidContext(applicationContext)
            modules(retrofitModule, viewModelModule)
        }
    }
}