package net.auliaisb.sawitproweighbridgeticket.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.auliaisb.sawitproweighbridgeticket.data.room.TicketEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class TicketEntity(
    @PrimaryKey(autoGenerate = true)
    var _id: Long = 0,
    var key: String? = null,
    var dateTime: Long? = null,
    var licenseNumber: String? = null,
    var driverName: String? = null,
    var inboundWeight: Double? = null,
    var outboundWeight: Double? = null,
    var netWeight: Double? = null,
) {
    companion object {
        const val TABLE_NAME = "tickets"
    }
}