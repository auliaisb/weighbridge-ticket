package net.auliaisb.sawitproweighbridgeticket.data.model

data class Ticket(
    var key: String? = null,
    var id: Long? = null,
    var dateTime: Long? = null,
    var licenseNumber: String? = null,
    var driverName: String? = null,
    var inboundWeight: Double? = null,
    var outboundWeight: Double? = null,
    var netWeight: Double? = null,
)