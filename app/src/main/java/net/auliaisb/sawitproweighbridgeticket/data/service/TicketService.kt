package net.auliaisb.sawitproweighbridgeticket.data.service

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import net.auliaisb.sawitproweighbridgeticket.data.model.Ticket
import javax.inject.Inject


class TicketService @Inject constructor(
    private val db: FirebaseDatabase
) {
    suspend fun addTicket(ticket: Ticket): Result<Boolean> {
        val ref = db.reference.child("tickets")
        return try {
            ref.push().setValue(ticket).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun editTicket(ticket: Ticket): Result<Boolean> {
        val ref = db.reference.child("tickets")
        val key = ticket.key.orEmpty()
        return try {
            val ticketValues = ticket.toMap()
            val updates = hashMapOf<String, Any>(
                key to ticketValues
            )
            ref.updateChildren(updates).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getTicketList(): Flow<Result<List<Ticket>>> {
        val ref = db.reference.child("tickets")
        return callbackFlow {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.children.mapNotNull {
                        val ticket = it.getValue(Ticket::class.java)
                        ticket?.key = it.key
                        ticket
                    }
                    trySend(Result.success(result)).isSuccess
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Result.failure(error.toException()))
                    close(error.toException())
                }
            }

            ref.addValueEventListener(valueEventListener)
            awaitClose { ref.removeEventListener(valueEventListener) }
        }
    }
}