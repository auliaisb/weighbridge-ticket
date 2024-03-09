package net.auliaisb.sawitproweighbridgeticket.domain

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import net.auliaisb.sawitproweighbridgeticket.data.model.Ticket
import net.auliaisb.sawitproweighbridgeticket.data.service.TicketService
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
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
        if (ticket != null) {
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