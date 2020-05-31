package developer.chirag.covid19.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
import developer.chirag.covid19.R
import developer.chirag.covid19.api.response.DataResponse
import developer.chirag.covid19.databinding.ActivityMainBinding
import developer.chirag.covid19.models.StateWiseDetails
import developer.chirag.covid19.notification.NotificationWorkManager
import developer.chirag.covid19.ui.main.adapters.CountryReportAdapter
import developer.chirag.covid19.ui.main.adapters.StateReportAdapter
import developer.chirag.covid19.ui.stateDetails.StateDetailsActivity
import developer.chirag.covid19.utils.getLastUpdatedDisplay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

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

    private val countryReportAdapter = CountryReportAdapter()
    private val stateReportAdapter = StateReportAdapter(this::onStateClicked)
    private val recyclerViewAdapter = MergeAdapter(countryReportAdapter, stateReportAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        initView()

        initDataAndObserver()
        initWorkerNotification()
    }

    private fun initView() {
        setSupportActionBar(activityBinding.appBarlayout.toolbar)
        activityBinding.recyclerView.adapter = recyclerViewAdapter
        activityBinding.swipeRefreshLayout.setOnRefreshListener {
            loadCovid19Data()
        }
    }

    private fun initDataAndObserver() {
        mainActivityViewModel.covid19LiveData.observe(this, Observer { it ->
            when (it) {
                is DataResponse.Loading -> activityBinding.swipeRefreshLayout.isRefreshing = true
                is DataResponse.Success -> {
                    activityBinding.swipeRefreshLayout.isRefreshing = false

                    val dataList = it.data.stateWise
                    countryReportAdapter.submitList(dataList.subList(0, 1))
                    stateReportAdapter.submitList(dataList.subList(1, dataList.size - 1))
                    activityBinding.lastUpdateTimeTv.text = getString(
                        R.string.last_updated_time,
                        getLastUpdatedDisplay(dataList.get(0).lastUpdatedTime)
                    )
                }
                is DataResponse.Error -> {
                    activityBinding.swipeRefreshLayout.isRefreshing = false
                    backPressSnackbar.setText(it.message).show()
                }
            }

        })

        if (mainActivityViewModel.covid19LiveData.value !is DataResponse.Success) {
            mainActivityViewModel.getIndiaCovid19Data()
        }
    }


    private fun loadCovid19Data() {
        mainActivityViewModel.getIndiaCovid19Data()
    }

    private fun onStateClicked(stateWiseDetails: StateWiseDetails) {
        startActivity(Intent(this, StateDetailsActivity::class.java).apply {
            putExtra(StateDetailsActivity.KEY_SELECTED_STATE, stateWiseDetails)
        })
    }

    private fun initWorkerNotification() {
        val workerConstraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicNotificationWorkerRequest =
            PeriodicWorkRequestBuilder<NotificationWorkManager>(2, TimeUnit.HOURS)
                .setConstraints(workerConstraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            WORKMANAGER_JOB_TAG, ExistingPeriodicWorkPolicy.KEEP, periodicNotificationWorkerRequest
        )
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

    companion object {
        const val WORKMANAGER_JOB_TAG = "NotificationWorkManager"
    }
}
