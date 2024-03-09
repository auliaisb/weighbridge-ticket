package net.auliaisb.sawitproweighbridgeticket
import com.google.common.base.CharMatcher.any
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.Dispatchers
import net.auliaisb.sawitproweighbridgeticket.data.model.Ticket
import net.auliaisb.sawitproweighbridgeticket.data.repo.TicketRepository
import net.auliaisb.sawitproweighbridgeticket.domain.ListTicketViewModel
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class ListTicketViewModelTest {

    private lateinit var viewModel: ListTicketViewModel
    private val ticketRepository: TicketRepository = mock()
    private val listener: ListTicketViewModel.ListTicketEventListener = mock()
    private val dispatcher = Dispatchers.Unconfined

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        viewModel = ListTicketViewModel(ticketRepository, dispatcher)
        viewModel.listener = listener
    }

    @Test
    fun onEditClicked_ticketFound_callsEditTicket() = runBlockingTest {
        // Given
        val ticket = Ticket(
            id = 0,
            licenseNumber = "B1234C",
            driverName = "John Doe",
            netWeight = 3.0,
            outboundWeight = 3.0,
            inboundWeight = 6.0,
            dateTime = System.nanoTime(),
        )
        viewModel.ticketList = listOf(ticket)

        // When
        viewModel.onEditClicked(ticket.id)

        // Then
        verify(listener).editTicket(ticket)
    }

    @Test
    fun onEditClicked_ticketNotFound_callsShowError() = runBlockingTest {
        // Given no tickets in the list

        // When
        viewModel.onEditClicked(999) // Assume 999 is an ID not in ticketList

        // Then
        verify(listener).showError(any().toString())
    }
}