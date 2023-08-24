package com.example.shacklehotelbuddy.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shacklehotelbuddy.R
import com.example.shacklehotelbuddy.base.BaseFragment
import com.example.shacklehotelbuddy.data.local.entity.SearchQueryEntity
import com.example.shacklehotelbuddy.data.mapper.Mapper.mapToUiModel
import com.example.shacklehotelbuddy.data.remote.model.Children
import com.example.shacklehotelbuddy.data.remote.model.Date
import com.example.shacklehotelbuddy.data.remote.model.PropertiesRequestBody
import com.example.shacklehotelbuddy.data.remote.model.Rooms
import com.example.shacklehotelbuddy.databinding.HomeFragmentBinding
import androidx.lifecycle.flowWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.example.shacklehotelbuddy.utils.Constants.KEY_REQUEST_BODY
import com.example.shacklehotelbuddy.utils.convertDateToValidFormat

import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> HomeFragmentBinding
        get() = HomeFragmentBinding::inflate

    private val viewModel: HomeViewModel by viewModels()

    private val calendar = Calendar.getInstance()
    private var propertiesRequestBody = PropertiesRequestBody()

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
        viewModel.getRecentSearchQuery()
    }

    private fun initObservations() {
        viewModel.recentSearchQueryLiveData.observe(viewLifecycleOwner) {
            it?.let {
                propertiesRequestBody = it.mapToUiModel()
            }
            updateRecentSearchView(it)
        }
        viewModel.searchQueryLiveData.observe(viewLifecycleOwner) {
            // query saved in local storage now navigate
            val itemBundle = bundleOf(KEY_REQUEST_BODY to propertiesRequestBody)
            findNavController().navigate(R.id.hotelsFragment, itemBundle)
        }
    }

    private fun initListener() {
        binding.btnCheckInDate.setOnClickListener {
            showDatePickerDialog(DateSelection.CHECK_IN)
        }
        binding.btnCheckOutDate.setOnClickListener {
            showDatePickerDialog(DateSelection.CHECKOUT)
        }
        binding.btnSearch.setOnClickListener {
            if (validateRequestBody()) {
                val roomsList = arrayListOf<Rooms>()
                val childrenList = arrayListOf<Children>()
                if (binding.etChildren.text.toString().trim().isNotEmpty()) {
                    childrenList.add(Children(age = binding.etChildren.text.toString().toInt()))
                }
                roomsList.add(Rooms(
                    adults = binding.etAdult.text.toString().toInt(),
                    children = childrenList
                ))
                propertiesRequestBody.rooms = roomsList

                viewModel.saveSearchAndProceed(propertiesRequestBody)
            }
        }

        binding.tvManageHistory.setOnClickListener {

            if (propertiesRequestBody.checkInDate != null &&
                propertiesRequestBody.checkOutDate != null
            ) {
                val adultsCount =
                    propertiesRequestBody.rooms?.firstOrNull()?.adults.toString() ?: ""
                var editableText = Editable.Factory.getInstance().newEditable(adultsCount)
                binding.etAdult.text = editableText

                val childrenCount =
                    propertiesRequestBody.rooms?.firstOrNull()?.children?.firstOrNull()?.age ?: ""
                editableText = Editable.Factory.getInstance().newEditable(childrenCount.toString())
                binding.etChildren.text = editableText

                propertiesRequestBody.checkInDate?.let {
                    binding.btnCheckInDate.text = convertDateToValidFormat(it)
                }
                propertiesRequestBody.checkOutDate?.let {
                    binding.btnCheckOutDate.text = convertDateToValidFormat(it)
                }
            }
        }


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun showDatePickerDialog(dateSelection: DateSelection) {
        val datePickerListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = LocalDate.of(year,
                    monthOfYear + 1,
                    dayOfMonth)
                updateDateInRequestBody(selectedDate, dateSelection)
            }
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            datePickerListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateDateInRequestBody(selectedDate: LocalDate, dateSelection: DateSelection) {

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.US)
        val formattedDate = formatter.format(selectedDate)

        val day = selectedDate.dayOfMonth
        val month = selectedDate.monthValue
        val year = selectedDate.year
        val date = Date(day, month, year)

        when (dateSelection) {
            DateSelection.CHECK_IN -> {
                propertiesRequestBody.checkInDate = date
                binding.btnCheckInDate.text = formattedDate
            }
            DateSelection.CHECKOUT -> {
                propertiesRequestBody.checkOutDate = date
                binding.btnCheckOutDate.text = formattedDate
            }
        }
    }

    private fun validateRequestBody(): Boolean {
        binding.btnCheckInDate.error = null
        binding.btnCheckOutDate.error = null
        binding.etAdult.error = null
        if (propertiesRequestBody.checkInDate == null) {
            binding.btnCheckInDate.error = "Select CheckIn Date"
            return false
        }
        if (propertiesRequestBody.checkOutDate == null) {
            binding.btnCheckOutDate.error = "Select CheckOut Date"
            return false
        }
        if (binding.etAdult.text!!.toString().trim().isEmpty() || binding.etAdult.text!!.trim()
                .toString()
                .toInt() <= 0
        ) {
            binding.etAdult.error = "Enter valid no of Adults"
            return false
        }

        return true
    }

    private fun updateRecentSearchView(query: SearchQueryEntity?) {
        query?.let {
            binding.tvManageHistory.text =
                buildString {
                    append(query.checkInDate)
                    append("  -  ")
                    append(query.checkOutDate)
                    append("  ")
                    append(query.adults)
                    append(" adults , ")
                    append(query.children)
                    append(" children")
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

    private enum class DateSelection {
        CHECK_IN,
        CHECKOUT
    }
}