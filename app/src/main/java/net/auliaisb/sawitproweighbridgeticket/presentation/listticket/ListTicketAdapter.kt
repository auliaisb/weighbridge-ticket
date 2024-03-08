package net.auliaisb.sawitproweighbridgeticket.presentation.listticket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.auliaisb.sawitproweighbridgeticket.databinding.ItemTicketBinding
import net.auliaisb.sawitproweighbridgeticket.domain.UITicket

class ListTicketAdapter :
    ListAdapter<UITicket, ListTicketAdapter.ListTicketViewHolder>(TicketDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTicketViewHolder {
        val itemBinding = ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListTicketViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListTicketViewHolder, position: Int) {
        holder.onBind(getItem(position), {

        })
    }

    class TicketDiffCallback : DiffUtil.ItemCallback<UITicket>() {
        override fun areItemsTheSame(oldItem: UITicket, newItem: UITicket) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: UITicket, newItem: UITicket) =
            oldItem == newItem
    }

    class ListTicketViewHolder(private val itemBinding: ItemTicketBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(uiTicket: UITicket, onEditClick: () -> Unit) {
            itemBinding.root.setOnClickListener {
                itemBinding.detailView.visibility =
                    if(itemBinding.detailView.visibility == View.GONE) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
            itemBinding.editBtn.setOnClickListener {

            }
            with(uiTicket) {
                itemBinding.dateTimeText.text = dateTime
                itemBinding.driverNameText.text = driverName
                itemBinding.netWeightText.text = netWeight
                itemBinding.inboundWeightText.text = inboundWeight
                itemBinding.outboundWeightText.text = outboundWeight
                itemBinding.licenseText.text = license
            }
        }
    }
}