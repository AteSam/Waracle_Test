package com.example.waracleandroidtest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.waracleandroidtest.adapter.CakeListAdapter
import com.example.waracleandroidtest.databinding.FragmentCakeListBinding
import com.example.waracleandroidtest.viewmodel.CakeEvent
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

    private val adapter = CakeListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCakeListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cakeViewModel.cakeEvent.onEach { processEvent(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        cakeViewModel.getCakes()
    }


    private fun processEvent(event: CakeEvent) {
        when (event) {
            CakeEvent.CakeLoding -> {
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
        }
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