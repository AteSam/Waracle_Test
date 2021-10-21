package com.example.waracleandroidtest.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.waracleandroidtest.adapter.CakeListAdapter
import com.example.waracleandroidtest.databinding.FragmentCakeListBinding
import com.example.waracleandroidtest.domain.entity.Cake
import com.example.waracleandroidtest.R
import com.example.waracleandroidtest.viewmodel.CakeEvent
import com.example.waracleandroidtest.viewmodel.CakeItemAction
import com.example.waracleandroidtest.viewmodel.CakeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * A simple [Fragment] subclass.
 * Use the [CakeListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CakeListFragment : Fragment() {

    private lateinit var binding: FragmentCakeListBinding

    private val cakeViewModel: CakeViewModel by viewModels()

    private lateinit var adapter: CakeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        binding = FragmentCakeListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CakeListAdapter {
            cakeViewModel.submitAction(CakeItemAction.CakeItemClicked(it))
        }
        cakeViewModel.cakeEvent.onEach { processEvent(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        cakeViewModel.submitAction(CakeItemAction.FetchCakes)
    }


    private fun processEvent(event: CakeEvent) {
        when (event) {
            CakeEvent.CakeLoading -> {
                binding.apply {
                    progress.isVisible = true
                    error.isGone = true
                }
            }

            CakeEvent.CakeError -> {
                binding.apply {
                    progress.isGone = true
                    error.isVisible = true
                }
            }

            is CakeEvent.CakeLoaded -> {
                binding.apply {
                    progress.isGone = true
                    error.isGone = true
                }
                binding.cakesRecyclerView.adapter = adapter
                adapter.submitList(event.data)
            }

            is CakeEvent.CakeItemClickEvent -> showDialog(event.cake)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.refreshmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                cakeViewModel.submitAction(CakeItemAction.FetchCakes)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialog(cake: Cake) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(cake.title)
        builder.setMessage(cake.desc)

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

        }

        builder.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment CakeListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            CakeListFragment()
    }
}