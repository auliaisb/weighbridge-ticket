package net.auliaisb.sawitproweighbridgeticket.presentation.listticket

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.auliaisb.sawitproweighbridgeticket.Extensions.dpToPx
import net.auliaisb.sawitproweighbridgeticket.R
import net.auliaisb.sawitproweighbridgeticket.databinding.FragmentListTicketBinding
import net.auliaisb.sawitproweighbridgeticket.domain.ListTicketViewModel
import net.auliaisb.sawitproweighbridgeticket.utils.MarginItemDecoration

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ListTicketPage : Fragment() {

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

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner)

        _binding = FragmentListTicketBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                viewModel.ticketListFlow.collect { listTicket ->
                    listAdapter.submitList(listTicket)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}