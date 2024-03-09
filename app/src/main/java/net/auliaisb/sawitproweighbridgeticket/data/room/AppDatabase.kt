package net.auliaisb.sawitproweighbridgeticket.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TicketEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ticketDao(): TicketDao
}