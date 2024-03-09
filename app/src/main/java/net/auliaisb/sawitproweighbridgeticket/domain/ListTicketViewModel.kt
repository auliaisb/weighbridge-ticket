package net.auliaisb.sawitproweighbridgeticket.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.base.CharMatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.auliaisb.sawitproweighbridgeticket.data.model.Ticket
import net.auliaisb.sawitproweighbridgeticket.data.repo.TicketRepository
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ListTicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val ticketListFlow = ticketRepository.ticketList
    var ticketList = emptyList<Ticket>()
    var listener: ListTicketEventListener? = null

    init {
        viewModelScope.launch {
            ticketRepository.syncTicketsWithFirebase()
        }
    }

    fun onEditClicked(id: Long?) {
        val ticket = ticketList.find { it.id == id }
        if (ticket != null) {
            listener?.editTicket(ticket)
        } else {
            listener?.showError(Exception("Not found ticket with specific key").message.orEmpty())
        }
    }

    suspend fun getTicketList() {
        withContext(dispatcher) {
            ticketListFlow.collect { list ->
                ticketList = list
                listener?.showData(
                    ticketList.map { ticket ->
                        UITicket.from(ticket)
                    }
                )
            }
        }
    }

    fun filter(date: LocalDate?, name: String?, license: String?) {
        listener?.showData(ticketList.filter {
            if (!name.isNullOrEmpty()) {
                it.driverName?.contains(name) ?: false
            } else {
                true
            }
        }.filter {
            if (date != null) {
                val itemDate = LocalDate.ofInstant(
                    it.dateTime?.let { it1 -> Instant.ofEpochMilli(it1) },
                    ZoneId.systemDefault()
                )
                val dayDate = LocalDate.of(itemDate.year, itemDate.month, itemDate.dayOfMonth)
                dayDate.equals(date)
            } else {
                true
            }
        }.filter {
            if (!license.isNullOrEmpty()) {
                it.licenseNumber?.contains(license) ?: false
            } else {
                true
            }
        }.map {
            UITicket.from(it)
        })
    }

    fun sortByDate() {
        listener?.showData(
            ticketList.sortedByDescending { it.dateTime }.map { UITicket.from(it) }
        )
    }

    fun sortByName() {
        listener?.showData(
            ticketList.sortedBy { it.driverName }.map { UITicket.from(it) }
        )
    }

    fun sortByLicense() {
        listener?.showData(
            ticketList.sortedBy { it.licenseNumber }.map { UITicket.from(it) }
        )
    }

    interface ListTicketEventListener {
        fun showData(listTicket: List<UITicket>?)
        fun editTicket(ticket: Ticket)
        fun showError(message: String)
    }
}