package net.auliaisb.sawitproweighbridgeticket.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Insert
    suspend fun insertTicket(ticket: TicketEntity): Long

    @Update
    suspend fun updateTicket(ticket: TicketEntity)

    @Query("SELECT * FROM tickets")
    fun getAllTickets(): Flow<List<TicketEntity>>
}