package net.auliaisb.sawitproweighbridgeticket

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import kotlinx.coroutines.test.setMain
import org.mockito.ArgumentMatchers.any
import kotlinx.coroutines.Dispatchers
import net.auliaisb.sawitproweighbridgeticket.data.model.Ticket
import net.auliaisb.sawitproweighbridgeticket.data.repo.TicketRepository
import net.auliaisb.sawitproweighbridgeticket.domain.AddEditTicketViewModel
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class AddEditTicketViewModelTest {

    private lateinit var viewModel: AddEditTicketViewModel
    private val ticketRepository: TicketRepository = mock()
    private val addTicketPageListener: AddEditTicketViewModel.AddTicketPageListener = mock()

    @Before
    fun setup() {
        // Use a test dispatcher for coroutines
        Dispatchers.setMain(Dispatchers.Unconfined)

        // Initialize your ViewModel here
        viewModel = AddEditTicketViewModel(ticketRepository, Dispatchers.Unconfined)
        viewModel.addTicketPageListener = addTicketPageListener
    }

    @Test
    fun sendTicket_successSubmit_callsOnSuccessSubmit() = runBlockingTest {
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
        `when`(ticketRepository.addTicket(any())).thenReturn(Result.success(mock()))

        // When
        viewModel.sendTicket("licenseNum", "driverName")

        // Then
        verify(addTicketPageListener).onSuccessSubmit()
    }

    @Test
    fun sendTicket_errorOccurs_callsOnError() = runBlockingTest {
        // Given
        val errorMessage = "Error occurred"
        `when`(ticketRepository.addTicket(any())).thenReturn(Result.failure(Exception(errorMessage)))

        // When
        viewModel.sendTicket("licenseNum", "driverName")

        // Then
        verify(addTicketPageListener).onError(errorMessage)
    }
}
