package net.auliaisb.sawitproweighbridgeticket.data.model

import com.google.firebase.database.Exclude
import net.auliaisb.sawitproweighbridgeticket.data.room.TicketEntity
import java.io.Serializable

data class Ticket(
    var id: Long? = null,
    var key: String? = null,
    var dateTime: Long? = null,
    var licenseNumber: String? = null,
    var driverName: String? = null,
    var inboundWeight: Double? = null,
    var outboundWeight: Double? = null,
    var netWeight: Double? = null,
) : Serializable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            ID to id,
            DATE_TIME to dateTime,
            LICENSE_NUMBER to licenseNumber,
            DRIVER_NAME to driverName,
            INBOUND_WEIGHT to inboundWeight,
            OUTBOUND_WEIGHT to outboundWeight,
            NET_WEIGHT to netWeight
        )
    }

    @Exclude
    fun toEntity(): TicketEntity {
        val tid = id
        return if(tid != null) {
            TicketEntity(
                _id = tid,
                key = key,
                dateTime = dateTime,
                licenseNumber = licenseNumber,
                driverName = driverName,
                inboundWeight = inboundWeight,
                outboundWeight = outboundWeight,
                netWeight = netWeight
            )
        } else {
            TicketEntity(
                key = key,
                dateTime = dateTime,
                licenseNumber = licenseNumber,
                driverName = driverName,
                inboundWeight = inboundWeight,
                outboundWeight = outboundWeight,
                netWeight = netWeight
            )
        }
    }

    companion object {
        const val ID = "id"
        const val DATE_TIME = "dateTime"
        const val LICENSE_NUMBER = "licenseNumber"
        const val DRIVER_NAME = "driverName"
        const val INBOUND_WEIGHT = "inboundWeight"
        const val OUTBOUND_WEIGHT = "outboundWeight"
        const val NET_WEIGHT = "netWeight"

        fun fromEntity(ticketEntity: TicketEntity): Ticket {
            return Ticket(
                id = ticketEntity._id,
                key = ticketEntity.key,
                dateTime = ticketEntity.dateTime,
                licenseNumber = ticketEntity.licenseNumber,
                driverName = ticketEntity.driverName,
                inboundWeight = ticketEntity.inboundWeight,
                outboundWeight = ticketEntity.outboundWeight,
                netWeight = ticketEntity.netWeight
            )
        }
    }
}