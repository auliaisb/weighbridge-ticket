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
    var listener: ListTicketAdapterInterface? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTicketViewHolder {
        val itemBinding =
            ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListTicketViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListTicketViewHolder, position: Int) {
        holder.onBind(getItem(position)) {
            listener?.onEditClicked(it)
        }
    }

    class TicketDiffCallback : DiffUtil.ItemCallback<UITicket>() {
        override fun areItemsTheSame(oldItem: UITicket, newItem: UITicket) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: UITicket, newItem: UITicket) =
            oldItem == newItem
    }

    class ListTicketViewHolder(private val itemBinding: ItemTicketBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(uiTicket: UITicket, onEditClick: (Long?) -> Unit) {
            itemBinding.root.setOnClickListener {
                val visibility = itemBinding.detailView.visibility
                val toggleTo = if (visibility == View.GONE) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

                itemBinding.detailView.visibility = toggleTo
                itemBinding.editBtn.visibility = toggleTo
            }

            itemBinding.editBtn.setOnClickListener {
                onEditClick.invoke(uiTicket.id)
            }

            with(uiTicket) {
                val netWeightText = "Net weight: $netWeight"
                val inboundWeightText = "Inbound weight $inboundWeight"
                val outboundWeightText = "Outbound weight: $outboundWeight"

                itemBinding.dateTimeText.text = dateTime
                itemBinding.driverNameText.text = driverName
                itemBinding.netWeightText.text = netWeightText
                itemBinding.inboundWeightText.text = inboundWeightText
                itemBinding.outboundWeightText.text = outboundWeightText
                itemBinding.licenseText.text = license
            }
        }
    }

    interface ListTicketAdapterInterface {
        fun onEditClicked(id: Long?)
    }
}