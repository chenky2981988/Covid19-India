package developer.chirag.covid19.ui.stateDetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import developer.chirag.covid19.databinding.StateWiseReportBinding
import developer.chirag.covid19.models.DistrictData


/**
 * Created by Chirag Sidhiwala on 30/5/20.
 */
class DistrictAdapter() :
    ListAdapter<DistrictData, DistrictAdapter.DistrictViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictViewHolder {
        return DistrictViewHolder(
            StateWiseReportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DistrictViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DistrictViewHolder(private val binding: StateWiseReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(districtData: DistrictData) {
            binding.stateLastUpdatedTime.visibility = View.GONE
            binding.stateNameTv.text = districtData.district
            binding.stateConfirmedCasesTv.text = districtData.confirmed.toString()
            binding.stateRecoveredTv.text = districtData.recovered.toString()
            binding.stateDeathTv.text = districtData.deceased.toString()
            binding.stateActiveCasesTv.text = districtData.active.toString()

            districtData.delta.confirmed.let {
                if (it == 0) {
                    binding.stateConfirmedGroup.visibility = View.GONE
                } else {
                    binding.stateConfirmedGroup.visibility = View.VISIBLE
                    binding.stateNewConfirmedTv.text = it.toString()
                }
            }

            districtData.delta.recovered.let {
                if (it == 0) {
                    binding.stateRecoveredGroup.visibility = View.GONE
                } else {
                    binding.stateRecoveredGroup.visibility = View.VISIBLE
                    binding.stateNewRecoveredCasesTv.text = it.toString()
                }
            }

            districtData.delta.deceased.let {
                if (it == 0) {
                    binding.stateDeathGroup.visibility = View.GONE
                } else {
                    binding.stateDeathGroup.visibility = View.VISIBLE
                    binding.stateNewDeathTv.text = it.toString()
                }
            }

        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DistrictData>() {
            override fun areItemsTheSame(
                oldItem: DistrictData,
                newItem: DistrictData
            ): Boolean {
                return oldItem.district == newItem.district
            }

            override fun areContentsTheSame(
                oldItem: DistrictData,
                newItem: DistrictData
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}