package developer.chirag.covid19.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import developer.chirag.covid19.databinding.CountryReportBinding
import developer.chirag.covid19.models.StateWiseDetails


/**
 * Created by Chirag Sidhiwala on 30/5/20.
 */
class CountryReportAdapter() :
    ListAdapter<StateWiseDetails, CountryReportAdapter.CountryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(CountryReportBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CountryViewHolder(private val binding: CountryReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stateWiseDetails: StateWiseDetails) {
            binding.activeCasesTv.text = stateWiseDetails.active
            binding.totalConfirmedTv.text = stateWiseDetails.confirmed
            binding.recoveredCasesTv.text = stateWiseDetails.recovered
            binding.totalDeathTv.text = stateWiseDetails.deaths

            stateWiseDetails.deltaConfirmed.let {
                if(it == "0"){
                    binding.groupNewConfirmed.visibility = View.GONE
                } else {
                    binding.groupNewConfirmed.visibility = View.VISIBLE
                    binding.newConfirmedCaseTv.text = it
                }
            }

            stateWiseDetails.deltaRecovered.let {
                if(it == "0"){
                    binding.groupNewRecovered.visibility = View.GONE
                } else {
                    binding.groupNewRecovered.visibility = View.VISIBLE
                    binding.newRecoveredTv.text = it
                }
            }

            stateWiseDetails.deltaDeaths.let {
                if(it == "0"){
                    binding.groupNewDeaths.visibility = View.GONE
                } else {
                    binding.groupNewDeaths.visibility = View.VISIBLE
                    binding.newDeathTv.text = it
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StateWiseDetails>() {
            override fun areItemsTheSame(
                oldItem: StateWiseDetails,
                newItem: StateWiseDetails
            ): Boolean {
                return oldItem.state == newItem.state
            }

            override fun areContentsTheSame(
                oldItem: StateWiseDetails,
                newItem: StateWiseDetails
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}