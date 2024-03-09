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
    db: FirebaseDatabase
) {
    private val ref = db.reference.child(TICKETS)
    suspend fun addTicket(ticket: Ticket): Result<String?> {
        return try {
            val nodeRef = ref.push().key
            ref.child(nodeRef.orEmpty()).setValue(ticket).await()
            Result.success(nodeRef)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun editTicket(ticket: Ticket): Result<String?> {
        val key = ticket.key.orEmpty()
        return try {
            val ticketValues = ticket.toMap()
            val updates = hashMapOf<String, Any>(
                key to ticketValues
            )
            ref.updateChildren(updates).await()
            Result.success(key)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getTicketList(): Flow<Result<List<Ticket>>> {
        return callbackFlow {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val result = snapshot.children.mapNotNull {
                            val ticket = it.getValue(Ticket::class.java)
                            ticket?.key = it.key
                            ticket
                        }
                        trySend(Result.success(result)).isSuccess
                    } catch (e: Exception) {
                        trySend(Result.failure(e))
                    }
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

    companion object {
        const val TICKETS = "tickets"
    }
}