package com.example.shacklehotelbuddy.ui.hotels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shacklehotelbuddy.R
import com.example.shacklehotelbuddy.adapters.PropertiesAdapter
import com.example.shacklehotelbuddy.base.BaseFragment
import com.example.shacklehotelbuddy.data.remote.model.PropertiesRequestBody
import com.example.shacklehotelbuddy.databinding.HotelsFragmentBinding
import com.example.shacklehotelbuddy.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HotelsFragment : BaseFragment<HotelsFragmentBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> HotelsFragmentBinding
        get() = HotelsFragmentBinding::inflate

    private val viewModel: HotelsViewModel by viewModels()
    private lateinit var propertiesAdapter: PropertiesAdapter
    private var requestBody = PropertiesRequestBody()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        initView()
        initListener()
        collectFlows()
        initObservations()
    }

    private fun initView() {
        arguments?.let {
            requestBody = it.get(Constants.KEY_REQUEST_BODY) as PropertiesRequestBody
            viewModel.getProperties(propertiesRequestBody = requestBody)
        }
    }

    private fun initObservations() {
        viewModel.propertiesLiveData.observe(viewLifecycleOwner) {
            // query saved in local storage
            propertiesAdapter.differ.submitList(it)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initListener() {
        propertiesAdapter = PropertiesAdapter {
            // high order function to get info of clicked property and navigate to details
        }

        binding.rcProperties.run {
            this.adapter = propertiesAdapter
        }

        binding.ivBackBtn.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.swipeRefreshLayout.setOnRefreshListener {
            requestBody.run {
                viewModel.getProperties(propertiesRequestBody = this)
            }
        }

    }


    private fun collectFlows() {
        lifecycleScope.launch {
            launch {
                viewModel.responseMessage.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        showSnackbar(it, binding.rootView)
                    }
            }
        }
    }

}