package net.auliaisb.sawitproweighbridgeticket.domain

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import net.auliaisb.sawitproweighbridgeticket.data.model.Ticket
import net.auliaisb.sawitproweighbridgeticket.data.service.TicketService
import javax.inject.Inject

@HiltViewModel
class ListTicketViewModel @Inject constructor(
    private val ticketService: TicketService,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val ticketListFlow = ticketService.getTicketList()
    private var ticketList = emptyList<Ticket>()
    var listener: ListTicketEventListener? = null

    fun onEditClicked(id: String) {
        val ticket = ticketList.find { it.key == id }
        if(ticket!= null) {
            listener?.editTicket(ticket)
        } else {
            listener?.showError(Exception("Not found ticket with specific key").message.orEmpty())
        }
    }

    suspend fun getTicketList() {
        withContext(dispatcher) {
            ticketListFlow.collect { result ->
                when {
                    result.isSuccess -> {
                        result.getOrNull()?.let {
                            ticketList = it
                            listener?.showData(
                                ticketList.map { ticket ->
                                    UITicket.from(ticket)
                                }
                            )
                        }
                    }

                    else -> {
                        listener?.showError(result.exceptionOrNull()?.message.orEmpty())
                    }
                }
            }
        }
    }

    interface ListTicketEventListener {
        fun showData(listTicket: List<UITicket>?)
        fun editTicket(ticket: Ticket)
        fun showError(message: String)
    }
}