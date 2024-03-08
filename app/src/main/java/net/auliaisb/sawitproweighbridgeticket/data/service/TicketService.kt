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

//    fun getTicketList(onSuccess: (List<Ticket>) -> Unit, onFailure: (Exception) -> Unit) {
//        fun parseSnapshot(snapshot: DataSnapshot): LiveData<List<Ticket>> {
//            val tickets = mutableListOf<Ticket>()
//            for (ticketSnapshot in snapshot.children) {
//                val ticket = ticketSnapshot.getValue(Ticket::class.java)
//                ticket?.let {
//                    tickets.add(it)
//                }
//            }
//            return tickets
//        }
//
//        db.reference.child("tickets").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                onSuccess.invoke(parseSnapshot(snapshot))
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                onFailure.invoke(error.toException())
//            }
//        })
//    }

    fun getTicketList(): Flow<List<Ticket>> {
        val ref = db.reference.child("tickets")
        return callbackFlow {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.children.mapNotNull {
                        val ticket = it.getValue(Ticket::class.java)
                        ticket?.key = it.key
                        ticket
                    }
                    trySend(result).isSuccess
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }

            ref.addValueEventListener(valueEventListener)
            awaitClose { ref.removeEventListener(valueEventListener) }
        }
    }
}