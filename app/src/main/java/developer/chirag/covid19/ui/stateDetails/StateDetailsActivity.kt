package developer.chirag.covid19.ui.stateDetails

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import com.google.android.material.snackbar.Snackbar
import developer.chirag.covid19.R
import developer.chirag.covid19.api.response.DataResponse
import developer.chirag.covid19.databinding.ActivityStateDetailsBinding
import developer.chirag.covid19.models.StateWiseDetails
import developer.chirag.covid19.ui.main.adapters.CountryReportAdapter
import developer.chirag.covid19.ui.main.adapters.StateReportAdapter
import developer.chirag.covid19.ui.stateDetails.adapters.DistrictAdapter
import developer.chirag.covid19.utils.getLastUpdatedDisplay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class StateDetailsActivity : AppCompatActivity() {


    private lateinit var activityStateDetailsBinding: ActivityStateDetailsBinding
    private val stateDetailsViewModel: StateDetailsViewModel by viewModel()
    private val stateAdapter = CountryReportAdapter()
    private val districtAdapter = DistrictAdapter()
    private val stateDetailsAdapter = MergeAdapter(stateAdapter, districtAdapter)
    private val errorSnackbar by lazy {
        Snackbar.make(
            activityStateDetailsBinding.root,
            "Error in connection",
            Snackbar.LENGTH_SHORT
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_state_details)
        activityStateDetailsBinding = ActivityStateDetailsBinding.inflate(layoutInflater)
        setContentView(activityStateDetailsBinding.root)
        initView()
        initDistrictData()
    }

    private fun initView() {
        setSupportActionBar(activityStateDetailsBinding.appBarlayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true);
        activityStateDetailsBinding.stateRecyclerView.adapter = stateDetailsAdapter

        val stateWiseDetails: StateWiseDetails? = getSelectedState()
        stateWiseDetails?.let {
            supportActionBar?.title = it.state
            stateAdapter.submitList(listOf(it))
            activityStateDetailsBinding.lastUpdateTimeTv.text =
                getString(R.string.last_updated_time, getLastUpdatedDisplay(it.lastUpdatedTime))
        }

        activityStateDetailsBinding.swipeToRefreshLayout.setOnRefreshListener {
            loadDistrictData()
        }
    }

    private fun initDistrictData() {
        stateDetailsViewModel.stateCovid19LiveData.observe(this, Observer {
            when (it) {
                is DataResponse.Loading -> {
                    activityStateDetailsBinding.swipeToRefreshLayout.isRefreshing = true
                }
                is DataResponse.Success -> {
                    activityStateDetailsBinding.swipeToRefreshLayout.isRefreshing = false

                    val dataList = it.data.districtData
                    dataList.sortedByDescending { it.confirmed }.let { districtList ->
                        districtAdapter.submitList(districtList)
                    }
                }
                is DataResponse.Error -> {
                    activityStateDetailsBinding.swipeToRefreshLayout.isRefreshing = false
                    errorSnackbar.setText(it.message).show()
                }
            }
        })

        if(stateDetailsViewModel.stateCovid19LiveData.value !is DataResponse.Success) {
            loadDistrictData()
        }
    }

    private fun getSelectedState(): StateWiseDetails? =
        intent.getParcelableExtra(KEY_SELECTED_STATE)

    private fun loadDistrictData() {
        val selectedState = getSelectedState()
        selectedState?.state?.let {
            stateDetailsViewModel.getStateDistrictReport(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return true
    }

    companion object {
        const val KEY_SELECTED_STATE = "selected_state"
    }
}
