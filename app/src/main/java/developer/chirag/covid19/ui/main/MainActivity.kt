package developer.chirag.covid19.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import developer.chirag.covid19.R
import developer.chirag.covid19.api.response.DataResponse
import developer.chirag.covid19.databinding.ActivityMainBinding
import developer.chirag.covid19.utils.getLastUpdatedDisplay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var activityBinding: ActivityMainBinding
    private val mainActivityViewModel: MainActivityViewModel by viewModel()
    private var backPressTime = 0L
    private val backPressSnackbar by lazy {
        Snackbar.make(activityBinding.root, R.string.back_message, Snackbar.LENGTH_SHORT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        activityBinding.swipeRefreshLayout.setOnRefreshListener {
            loadCovid19Data()
        }

        initDataAndObserver()
    }

    private fun initDataAndObserver() {
        mainActivityViewModel.covid19LiveData.observe(this, Observer { it ->
            when (it) {
                is DataResponse.Loading -> activityBinding.swipeRefreshLayout.isRefreshing = true
                is DataResponse.Success -> {
                    activityBinding.swipeRefreshLayout.isRefreshing = false

                    val dataList = it.data.stateWise
                    activityBinding.lastUpdateTimeTv.setText(getLastUpdatedDisplay(dataList.get(0).lastUpdatedTime))
                }
                is DataResponse.Error -> {
                    activityBinding.swipeRefreshLayout.isRefreshing = false
                    backPressSnackbar.setText(it.message).show()
                }
            }

        })

        if(mainActivityViewModel.covid19LiveData.value !is DataResponse.Success) {
            mainActivityViewModel.getIndiaCovid19Data()
        }
    }


    private fun loadCovid19Data() {
        mainActivityViewModel.getIndiaCovid19Data()
    }


    override fun onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            backPressSnackbar.dismiss()
            super.onBackPressed()
            return
        } else {
            backPressSnackbar.show()
        }
        backPressTime = System.currentTimeMillis()
    }
}
