package net.auliaisb.sawitproweighbridgeticket.domain

import net.auliaisb.sawitproweighbridgeticket.data.model.Ticket
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class UITicket(
    val id: Long?,
    val dateTime: String,
    val driverName: String,
    val netWeight: String,
    val inboundWeight: String,
    val outboundWeight: String,
    val license: String
) {
    companion object {
        fun from(ticket: Ticket): UITicket {
            val strDateTime = ticket.dateTime?.let {
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
                )
            }

            return UITicket(
                id = ticket.id,
                dateTime = strDateTime.orEmpty(),
                driverName = ticket.driverName.orEmpty(),
                netWeight = ticket.netWeight.toString(),
                inboundWeight = ticket.inboundWeight.toString(),
                outboundWeight = ticket.outboundWeight.toString(),
                license = ticket.licenseNumber.orEmpty()
            )
        }
    }
}