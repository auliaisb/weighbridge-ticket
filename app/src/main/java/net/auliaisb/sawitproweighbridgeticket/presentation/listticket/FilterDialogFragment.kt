package net.auliaisb.sawitproweighbridgeticket.presentation.listticket

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import net.auliaisb.sawitproweighbridgeticket.R
import net.auliaisb.sawitproweighbridgeticket.databinding.DialogFragmentFilterBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class FilterDialogFragment : DialogFragment() {

    private lateinit var filterName: EditText
    private lateinit var filterLicenseNumber: EditText
    private lateinit var filterDate: EditText

    private var _binding: DialogFragmentFilterBinding? = null
    private val binding get() = _binding!!
    var listener: FilterDialogListener? = null
    private var selectedDate: LocalDate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DialogFragmentFilterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterName = binding.driverNameText
        filterLicenseNumber = binding.licenseText
        filterDate = binding.dateText

        // Set up DatePickerDialog for the date filter
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                filterDate.setText(dateFormat.format(selectedDate))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        filterDate.setOnClickListener {
            datePickerDialog.show()
        }

        binding.submitBtn.setOnClickListener {
            // Here you would handle applying the filter based on the input values
            // For example, you might want to pass the filter criteria back to the
            // activity or fragment that opened this dialog
            applyFilters()
            dismiss()
        }
    }

    private fun applyFilters() {
        // Example: Use the inputted filter criteria as needed
        val name = filterName.text.toString()
        val licenseNumber = filterLicenseNumber.text.toString()

        // Pass filter criteria back to the activity/fragment or directly apply the filter
        listener?.onSubmitFilter(selectedDate, name, licenseNumber)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface FilterDialogListener {
        fun onSubmitFilter(date: LocalDate?, name: String?, licenseNumber: String?)
    }

    companion object {
        fun newInstance(): FilterDialogFragment {
            return FilterDialogFragment()
        }
    }
}