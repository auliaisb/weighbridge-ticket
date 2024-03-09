package net.auliaisb.sawitproweighbridgeticket.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import net.auliaisb.sawitproweighbridgeticket.data.model.Ticket
import net.auliaisb.sawitproweighbridgeticket.data.room.TicketDao
import net.auliaisb.sawitproweighbridgeticket.data.service.TicketService
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketService: TicketService,
    private val ticketDao: TicketDao
) {
    val ticketList: Flow<List<Ticket>> = ticketDao.getAllTickets().map {
        it.map { entity ->
            Ticket.fromEntity(entity)
        }
    }

    suspend fun addTicket(ticket: Ticket): Result<String?> {
        val entity = ticket.toEntity()
        val id = ticketDao.insertTicket(ticket.toEntity())
        val result = ticketService.addTicket(Ticket.fromEntity(entity.apply { _id = id}))

        return result
    }

    suspend fun editTicket(ticket: Ticket): Result<String?> {
        ticketDao.updateTicket(ticket.toEntity())
        val result = ticketService.editTicket(ticket)

        return result
    }

    suspend fun syncTicketsWithFirebase() {
        updateAddLocalToFirebase()
        updateEditLocalToFirebase()
        ticketService.getTicketList().collect { result ->
            if (result.isSuccess) {
                result.getOrNull()?.forEach { ticket ->
                    ticketDao.insertTicket(ticket.toEntity())
                }
            } else {
                result.exceptionOrNull()
            }
        }
    }
    private suspend fun updateAddLocalToFirebase() {
        ticketDao.getAllTickets().map {
            it.map { entity ->
                Ticket.fromEntity(entity)
            }
        }.collect {
            it.forEach { ticket ->
                if(ticket.key == null) {
                    val result = ticketService.addTicket(ticket)
                    if(result.isSuccess) {
                        result.getOrNull()?.let {
                            ticket.key = it
                            ticketDao.updateTicket(ticket.toEntity())
                        }
                    }
                }
            }
        }


    }

    private suspend fun updateEditLocalToFirebase() {
        ticketDao.getAllTickets().map {
            it.map { entity ->
                Ticket.fromEntity(entity)
            }
        }.collect {
            it.forEach { ticket ->
                if(ticket.key != null) {
                    ticketService.editTicket(ticket)
                }
            }
        }
    }
}