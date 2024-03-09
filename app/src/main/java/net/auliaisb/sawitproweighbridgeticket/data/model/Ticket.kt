package net.auliaisb.sawitproweighbridgeticket.data.model

import com.google.firebase.database.Exclude
import java.io.Serializable

data class Ticket(
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
            "dateTime" to dateTime,
            "licenseNumber" to licenseNumber,
            "driverName" to driverName,
            "inboundWeight" to inboundWeight,
            "outboundWeight" to outboundWeight,
            "netWeight" to netWeight
        )
    }
}