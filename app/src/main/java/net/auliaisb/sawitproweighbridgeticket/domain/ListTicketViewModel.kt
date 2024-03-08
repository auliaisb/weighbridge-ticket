package net.auliaisb.sawitproweighbridgeticket.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.auliaisb.sawitproweighbridgeticket.data.service.TicketService
import javax.inject.Inject

@HiltViewModel
class ListTicketViewModel @Inject constructor(
    private val ticketService: TicketService,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    val ticketListFlow = ticketService.getTicketList().map { list ->
        list.map { ticket ->
            Log.d("listTicket", ticket.toString())
            UITicket.from(ticket)
        }
    }
}