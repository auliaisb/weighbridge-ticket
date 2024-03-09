package net.auliaisb.sawitproweighbridgeticket.presentation.listticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.auliaisb.sawitproweighbridgeticket.Extensions.dpToPx
import net.auliaisb.sawitproweighbridgeticket.R
import net.auliaisb.sawitproweighbridgeticket.data.model.Ticket
import net.auliaisb.sawitproweighbridgeticket.databinding.FragmentListTicketBinding
import net.auliaisb.sawitproweighbridgeticket.domain.ListTicketViewModel
import net.auliaisb.sawitproweighbridgeticket.domain.UITicket
import net.auliaisb.sawitproweighbridgeticket.utils.MarginItemDecoration
import java.time.LocalDate

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ListTicketPage : Fragment(), ListTicketViewModel.ListTicketEventListener, FilterDialogFragment.FilterDialogListener {

    private var _binding: FragmentListTicketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: ListTicketViewModel by viewModels()
    private val listAdapter: ListTicketAdapter = ListTicketAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.sort_name -> viewModel.sortByName()
                    R.id.sort_date -> viewModel.sortByDate()
                    R.id.sort_license -> viewModel.sortByLicense()
                    R.id.filter -> {
                        val filterDialog = FilterDialogFragment.newInstance()
                        filterDialog.listener = this@ListTicketPage
                        filterDialog.show(childFragmentManager, "filter")
                    }
                }
                return false
            }
        }, viewLifecycleOwner)
        viewModel.listener = this
        loadList()
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_ListTicketPage_to_AddTicketPage)
        }
    }

    private fun loadList() {
        binding.listItem.apply {
            addItemDecoration(MarginItemDecoration(8.dpToPx(context)))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = listAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getTicketList()
            }
        }
    }

    override fun showData(listTicket: List<UITicket>?) {
        viewLifecycleOwner.lifecycleScope.launch {
            listAdapter.submitList(listTicket)
            listAdapter.listener = object : ListTicketAdapter.ListTicketAdapterInterface {
                override fun onEditClicked(id: String) {
                    viewModel.onEditClicked(id)
                }
            }
            view?.post {
                binding.listItem.scrollToPosition(0)
            }
        }
    }

    override fun showError(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            getString(R.string.generic_error),
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun editTicket(ticket: Ticket) {
        val bundle = bundleOf("ticket" to ticket)
        findNavController().navigate(R.id.action_ListTicketPage_to_AddTicketPage, bundle)
    }

    override fun onSubmitFilter(date: LocalDate?, name: String?, licenseNumber: String?) {
        viewModel.filter(date, name, licenseNumber)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}