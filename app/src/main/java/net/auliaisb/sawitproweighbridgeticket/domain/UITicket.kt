package net.auliaisb.sawitproweighbridgeticket.domain

import net.auliaisb.sawitproweighbridgeticket.data.model.Ticket
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class UITicket(
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
                dateTime = strDateTime.orEmpty(),
                driverName = ticket.driverName.orEmpty(),
                netWeight = "Net weight: ${ticket.netWeight.toString()}",
                inboundWeight = "Inbound weight: ${ticket.inboundWeight.toString()}",
                outboundWeight = "Outbound weight: ${ticket.outboundWeight.toString()}",
                license = ticket.licenseNumber.orEmpty()
            )
        }
    }
}