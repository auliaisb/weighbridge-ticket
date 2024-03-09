package net.auliaisb.sawitproweighbridgeticket.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.auliaisb.sawitproweighbridgeticket.Extensions.round
import net.auliaisb.sawitproweighbridgeticket.R
import net.auliaisb.sawitproweighbridgeticket.data.model.Ticket
import net.auliaisb.sawitproweighbridgeticket.data.repo.TicketRepository
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddEditTicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    var addTicketPageListener: AddTicketPageListener? = null

    private var selectedDate: LocalDate = LocalDate.now()
    private var selectedTime: LocalTime = LocalTime.now()
    private var selectedDateTime: LocalDateTime = LocalDateTime.now()
    private var inboundWeight: Double = 0.0
    private var outboundWeight: Double = 0.0
    private var netWeight: Double = 0.0
    private var id: Long? = null
    private var key: String? = null

    fun init(ticket: Ticket?) {
        if (ticket == null) {
            showDate()
            showTime()
        } else {
            editTicket(ticket)
        }
    }

    private fun editTicket(ticket: Ticket) {
        key = ticket.key
        id = ticket.id
        ticket.dateTime?.let {
            selectedDateTime =
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
            setDate(selectedDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
            setTime(selectedDateTime.hour, selectedDateTime.minute)
        }
        addTicketPageListener?.populateForm(UITicket.from(ticket))
    }

    fun sendTicket(
        licenseNum: String,
        driverName: String,
    ) {

        if (!validateForm(licenseNum, driverName)) return

        selectedDateTime = LocalDateTime.of(selectedDate, selectedTime)

        val ticket = Ticket(
            key = key,
            id = id,
            dateTime = selectedDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            licenseNumber = licenseNum,
            driverName = driverName,
            inboundWeight = inboundWeight,
            outboundWeight = outboundWeight,
            netWeight = netWeight
        )

        viewModelScope.launch {
            val result: Result<String?>
            withContext(dispatcher) {
                if (id == null) {
                    result = ticketRepository.addTicket(ticket)
                } else {
                    result = ticketRepository.editTicket(ticket)
                }
            }
            result.onSuccess {
                addTicketPageListener?.onSuccessSubmit()
            }.onFailure {
                addTicketPageListener?.onError(it.message.orEmpty())
                addTicketPageListener?.finish()
            }
        }
    }

    private fun validateForm(
        licenseNum: String,
        driverName: String,
    ): Boolean {
        return when {
            licenseNum.isEmpty() -> {
                addTicketPageListener?.setErrorLicenseNumber(R.string.error_license_num_empty)
                false
            }

            driverName.isEmpty() -> {
                addTicketPageListener?.setErrorDriverName(R.string.error_driver_name_empty)
                false
            }

            inboundWeight == 0.0 -> {
                addTicketPageListener?.setErrorInboundWeight(R.string.error_invalid_weight)
                false
            }

            outboundWeight == 0.0 -> {
                addTicketPageListener?.setErrorOutboundWeight(R.string.error_invalid_weight)
                false
            }

            netWeight <= 0.0 -> {
                addTicketPageListener?.setErrorNetWeight(R.string.error_invalid_weight)
                false
            }

            else -> true
        }
    }

    fun setDate(millis: Long?) {
        setDateData(millis)
        showDate()
    }

    private fun setDateData(millis: Long?) {
        selectedDate =
            Instant.ofEpochMilli(millis ?: System.nanoTime()).atZone(ZoneId.systemDefault())
                .toLocalDate()
    }

    private fun showDate() {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val strDate = formatter.format(selectedDate)
        addTicketPageListener?.setDate(strDate)
    }

    fun setTime(hour: Int, minute: Int) {
        setTimeData(hour, minute)
        showTime()
    }

    private fun setTimeData(hour: Int, minute: Int) {
        selectedTime = LocalTime.of(hour, minute)
    }

    private fun showTime() {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val strTime = formatter.format(selectedTime)
        addTicketPageListener?.setTime(strTime)
    }

    fun setInboundOutboundWeight(newInboundWeight: String, newOutboundWeight: String) {
        inboundWeight = try {
            newInboundWeight.toDouble()
        } catch (e: Exception) {
            0.0
        }

        outboundWeight = try {
            newOutboundWeight.toDouble()
        } catch (e: Exception) {
            0.0
        }
        netWeight = calculateNetWeight(inboundWeight, outboundWeight)
        addTicketPageListener?.setNetWeightText(netWeight)
    }

    private fun calculateNetWeight(inboundWeight: Double, outboundWeight: Double): Double {
        return (inboundWeight - outboundWeight).round(2)
    }

    interface AddTicketPageListener {
        fun setNetWeightText(netWeight: Double)
        fun setDate(date: String)
        fun setTime(time: String)
        fun onError(message: String)
        fun setErrorLicenseNumber(stringRes: Int)
        fun setErrorDriverName(stringRes: Int)
        fun setErrorInboundWeight(stringRes: Int)
        fun setErrorOutboundWeight(stringRes: Int)
        fun setErrorNetWeight(stringRes: Int)
        fun onSuccessSubmit()
        fun finish()
        fun populateForm(ticket: UITicket)
    }
}