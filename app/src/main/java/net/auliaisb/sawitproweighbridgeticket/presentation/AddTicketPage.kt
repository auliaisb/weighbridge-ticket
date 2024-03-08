package net.auliaisb.sawitproweighbridgeticket.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import net.auliaisb.sawitproweighbridgeticket.R
import net.auliaisb.sawitproweighbridgeticket.databinding.FragmentAddTicketBinding
import net.auliaisb.sawitproweighbridgeticket.domain.AddTicketViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

@AndroidEntryPoint
class AddTicketPage : Fragment() {

    private val viewModel: AddTicketViewModel by viewModels()
    private var _binding: FragmentAddTicketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModelListener()
        viewModel.init()

        binding.licenseText.addTextChangedListener {
            binding.licenseLayout.error = null
        }

        binding.driverNameText.addTextChangedListener {
            binding.driverNameLayout.error = null
        }

        binding.inboundWeightText.addTextChangedListener {
            binding.inboundWeightLayout.error = null
            binding.netWeightLayout.error = null
            viewModel.setInboundOutboundWeight(
                it?.toString() ?: "0.0",
                binding.outboundWeightText.text.toString()
            )
        }

        binding.outboundWeightText.addTextChangedListener {
            binding.outboundWeightLayout.error = null
            binding.netWeightLayout.error = null
            viewModel.setInboundOutboundWeight(
                binding.inboundWeightText.text.toString(),
                it?.toString() ?: "0.0"
            )
        }

        binding.dateText.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                viewModel.setDate(datePicker.selection)
            }
            datePicker.show(childFragmentManager, "")
        }

        binding.timeText.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder().build()
            timePicker.addOnPositiveButtonClickListener {
                viewModel.setTime(timePicker.hour, timePicker.minute)
            }
            timePicker.show(childFragmentManager, "")
        }

        binding.submitBtn.setOnClickListener {
            viewModel.addTicket(
                binding.licenseText.text.toString(),
                binding.driverNameText.text.toString()
            )
        }
    }

    private fun setViewModelListener() {
        viewModel.addTicketPageListener = object : AddTicketViewModel.AddTicketPageListener {
            override fun setNetWeightText(netWeight: Double) {
                binding.netWeightText.setText(netWeight.toString())
            }

            override fun showLoading() {
                binding.loadingIndicator.show()
            }

            override fun hideLoading() {
                binding.loadingIndicator.hide()
            }

            override fun setDate(date: String) {
                binding.dateText.setText(date)
            }

            override fun setTime(time: String) {
                binding.timeText.setText(time)
            }

            override fun onError(message: String) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.success_submit_ticket),
                    Snackbar.LENGTH_LONG
                ).show()
            }

            override fun setErrorLicenseNumber(stringRes: Int) {
                binding.licenseLayout.error = getString(stringRes)
            }

            override fun setErrorDriverName(stringRes: Int) {
                binding.driverNameLayout.error = getString(stringRes)
            }

            override fun setErrorInboundWeight(stringRes: Int) {
                binding.inboundWeightLayout.error = getString(stringRes)
            }

            override fun setErrorOutboundWeight(stringRes: Int) {
                binding.outboundWeightLayout.error = getString(stringRes)
            }

            override fun setErrorNetWeight(stringRes: Int) {
                binding.netWeightLayout.error = getString(stringRes)
            }

            override fun onSuccessSubmit() {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.success_submit_ticket),
                    Snackbar.LENGTH_LONG
                ).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}