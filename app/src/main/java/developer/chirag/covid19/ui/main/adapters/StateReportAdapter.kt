package developer.chirag.covid19.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import developer.chirag.covid19.R
import developer.chirag.covid19.databinding.StateWiseReportBinding
import developer.chirag.covid19.models.StateWiseDetails
import developer.chirag.covid19.utils.getLastUpdatedDisplay


/**
 * Created by Chirag Sidhiwala on 30/5/20.
 */
class StateReportAdapter(val onItemClicked: (stateWiseDetails: StateWiseDetails) -> Unit = {}) :
    ListAdapter<StateWiseDetails, StateReportAdapter.StateViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        return StateViewHolder(
            StateWiseReportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StateViewHolder(private val binding: StateWiseReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stateWiseDetails: StateWiseDetails) {
            binding.stateNameTv.text = stateWiseDetails.state
            binding.stateLastUpdatedTime.text = itemView.context.getString(
                R.string.last_updated_time,
                getLastUpdatedDisplay(stateWiseDetails.lastUpdatedTime)
            )

            binding.stateConfirmedCasesTv.text = stateWiseDetails.confirmed
            binding.stateActiveCasesTv.text = stateWiseDetails.active
            binding.stateRecoveredTv.text = stateWiseDetails.recovered
            binding.stateDeathTv.text = stateWiseDetails.deaths

            stateWiseDetails.deltaConfirmed.let {
                if (it == "0") {
                    binding.stateConfirmedGroup.visibility = View.GONE
                } else {
                    binding.stateConfirmedGroup.visibility = View.VISIBLE
                    binding.stateNewConfirmedTv.text = it
                }
            }

            stateWiseDetails.deltaRecovered.let {
                if (it == "0") {
                    binding.stateRecoveredGroup.visibility = View.GONE
                } else {
                    binding.stateRecoveredGroup.visibility = View.VISIBLE
                    binding.stateNewRecoveredCasesTv.text = it
                }
            }

            stateWiseDetails.deltaDeaths.let {
                if (it == "0") {
                    binding.stateDeathGroup.visibility = View.GONE
                } else {
                    binding.stateDeathGroup.visibility = View.VISIBLE
                    binding.stateNewDeathTv.text = it
                }
            }

            binding.root.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }

                val item = getItem(bindingAdapterPosition)
                item.let {
                    onItemClicked.invoke(it)
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