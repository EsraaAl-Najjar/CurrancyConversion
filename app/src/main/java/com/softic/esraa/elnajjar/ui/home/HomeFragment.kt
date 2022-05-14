package com.softic.esraa.elnajjar.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.softic.esraa.elnajjar.R
import com.softic.esraa.elnajjar.data.model.ObjectResponse
import com.softic.esraa.elnajjar.data.model.Resource
import com.softic.esraa.elnajjar.databinding.FragmentHomeBinding
import com.softic.esraa.elnajjar.utils.PreferencesUtility
import com.softic.esraa.elnajjar.utils.Utility
import com.softic.esraa.elnajjar.utils.Utility.Companion.roundOfBigDecimal
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var dialogProgress: Dialog

    private lateinit var fromSymbolAdapter: ArrayAdapter<String>
    private lateinit var toSymbolAdapter: ArrayAdapter<String>
    private lateinit var symbolsKeysArrayList: ArrayList<String>

    private var selectedBaseFromSymbol: String = "USD"
    private var selectedToSymbol: String = ""

    private var fromAmountValue: Double = 1.0
    private var toAmountValue: Double = 0.0

    private var isFrom: Boolean = true
    private var baseEUR: String = "EUR"
    private lateinit var result: BigDecimal


    private var isFirstTimeForGetRequests = true // to not get the previous live data
    private var isFirstTimeForGetEURRequests = true // to not get the previous live data
    private lateinit var symbolsResponse: ObjectResponse
    private lateinit var eurResultResponse: ObjectResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        dialogProgress = Utility.getProgressDialog(requireContext())
        dialogProgress.show()

        setUpUI()
        setupObservers()

        return binding.root
    }

    private fun setUpUI() {

        binding.apply {
            btnSwapCurrency.setOnClickListener {
                val posFrom = spFromCurrency.selectedItemPosition
                spFromCurrency.setSelection(spToCurrency.selectedItemPosition)
                spToCurrency.setSelection(posFrom)
            }
        }
    }


    private fun setupObservers() {
        homeViewModel.symbolsResponse.observe(viewLifecycleOwner, Observer {

            when (it.status) {
                Resource.Status.LOADING -> {
                    isFirstTimeForGetRequests = false
                    dialogProgress.show()
                }

                Resource.Status.SUCCESS -> {
                    if (isFirstTimeForGetRequests) return@Observer
                    dialogProgress.dismiss()
                    if (it.data!!.symbols != null) {
                        Log.d(" HomeResp Symbols Currency  >>> ", it.data.toString())
                        symbolsResponse = it.data
                        val symbolsData = symbolsResponse.symbols!!.keys

                        symbolsKeysArrayList = ArrayList()
                        symbolsKeysArrayList.add(getString(R.string.default_currency))
                        symbolsKeysArrayList.addAll(symbolsData)
                        val currenciesCode: MutableList<String> = mutableListOf()
                        currenciesCode.addAll(symbolsData)
                        var currenciesString = currenciesCode.toString()
                        currenciesString =
                            currenciesString.replace(("[\\[ \\] \\  ]").toRegex(), "")

                        getAllValidCurrencyForEUR(currenciesString)

                        Log.d("  HomeResp Symboooooools>>> ", symbolsData.toString())
                        setUpSpinnerAdapter()
                    } else {
                        Log.d(" HomeResp Symbols error  >>> ", it.toString())
                    }
                }
                Resource.Status.ERROR -> {
                    if (isFirstTimeForGetRequests) return@Observer
                    dialogProgress.dismiss()
                    Log.d(" HomeResp Symbols error  >>> ", it.data!!.error.toString())
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getAllValidCurrencyForEUR(symbolsData: String) {
        homeViewModel.getCurrenciesForEURToConvertForOthersLocally(baseEUR, symbolsData.trim())
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        isFirstTimeForGetEURRequests = false
                        dialogProgress.show()
                    }

                    Resource.Status.SUCCESS -> {
                        Log.d(" HomeResp resultsConvert = ", it.toString())
                        if (isFirstTimeForGetEURRequests) return@Observer
                        dialogProgress.dismiss()
                        if (it.data!!.rates != null) {
                            eurResultResponse = it.data
                            PreferencesUtility.setString(
                                requireContext(),
                                "LATEST_CURRENCY_VALUE",
                                eurResultResponse.rates.toString()
                            )
                            setUpSpinnesrsListener()

                            Log.d(
                                " HomeResp getCurrenciesForEURToConvertForOthersLocally = ",
                                eurResultResponse.date.toString()
                            )

                        } else {
                            Log.d(" HomeResp Symbols error  >>> ", it.toString())
                        }
                    }
                    Resource.Status.ERROR -> {
                        Log.d("  HomeResp resultsConvert Error = ", it.responseErrorBody.toString())
                        dialogProgress.dismiss()
                        Log.d(" HomeResp error  >>> ", it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })

    }

    private fun setUpSpinnesrsListener() {
        binding.apply {
            spFromCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (symbolsKeysArrayList[position] != getString(R.string.default_currency)) {
                        //call api

                        selectedBaseFromSymbol = spFromCurrency.selectedItem.toString()
                        if (selectedBaseFromSymbol.isNotEmpty()) {
                            if (validate()) {

                                getConvertedAmount()
                            }

                        }
                        Toast.makeText(
                            requireContext(),
                            "changed from $selectedBaseFromSymbol",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
            spToCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (symbolsKeysArrayList[position] != getString(R.string.default_currency)) {
                        //call api
                        selectedToSymbol = spToCurrency.selectedItem.toString()
                        binding.etTo.text!!.clear()

                        if (validate()) {
                            getConvertedAmount()
                        }

                        Toast.makeText(requireContext(), "changed To", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun setUpSpinnerAdapter() {
        binding.apply {
            fromSymbolAdapter =
                ArrayAdapter(requireContext(), R.layout.item_spinner_list, symbolsKeysArrayList)
            this.spFromCurrency.adapter = fromSymbolAdapter
            spFromCurrency.setSelection(symbolsKeysArrayList.indexOf(getString(R.string.most_used_currency)))

            toSymbolAdapter =
                ArrayAdapter(requireContext(), R.layout.item_spinner_list, symbolsKeysArrayList)
            this.spToCurrency.adapter = toSymbolAdapter
        }
    }

    private fun validate(): Boolean {
        if (selectedBaseFromSymbol.isEmpty()) {
            Utility.showShortToast(requireContext(), "Please Select right Currency to Convert From")
        } else {
            if (selectedToSymbol.isEmpty()) {
                Utility.showShortToast(
                    requireContext(),
                    "Please Select right Currency to Convert To"
                )
            } else return true
            return true
        }



            if (binding.etFrom.text!!.isEmpty()) {
                if (binding.etTo.text!!.isEmpty()) {
                    binding.etFrom.error = getString(R.string.err_required_value_from)
                    binding.etTo.error = getString(R.string.err_required_value_to)
                } else {
                    binding.etTo.error = null
                    toAmountValue = binding.etTo.text.toString().toDouble()
                    return true
                }
            } else {
                binding.etFrom.error = null
                if (binding.etTo.text!!.isEmpty()) {
                    binding.etTo.error = getString(R.string.err_required_value_to)
                } else {
                    binding.etTo.error = null
                    toAmountValue = binding.etTo.text.toString().toDouble()
                    return true
                }
                fromAmountValue = binding.etFrom.text.toString().toDouble()
                return true
            }

        return false
    }

    private fun getConvertedAmount() {
        lateinit var mainChangerFrom: BigDecimal
        lateinit var amountToConvert: BigDecimal

        if (toAmountValue != 0.0) {
            amountToConvert = BigDecimal(toAmountValue)
            isFrom = false
        } else {
            amountToConvert = BigDecimal(fromAmountValue)
            isFrom = true

        }
        val fromBase = BigDecimal(eurResultResponse.rates!![selectedBaseFromSymbol] ?: 0.0)
        val toBase = BigDecimal(eurResultResponse.rates!![selectedToSymbol] ?: 0.0)

        when (isFrom) {
            true -> {
                amountToConvert = BigDecimal(fromAmountValue)
                mainChangerFrom = toBase.div(fromBase)
            }
            false -> {
                amountToConvert = BigDecimal(toAmountValue)
                mainChangerFrom = fromBase.div(toBase)
            }
        }

        result = amountToConvert.multiply(mainChangerFrom)
        updateCurrencyValue()

    }


    @SuppressLint("SetTextI18n")
    private fun updateCurrencyValue() {
        binding.apply {
            when (isFrom) {
                true -> {

                    if (result.equals(null)) etTo.setText("0")
                    else etTo.setText(roundOfBigDecimal(result).toString())

                }
                false -> {

                    if (result.equals(null)) etFrom.setText("0")
                    else etFrom.setText(roundOfBigDecimal(result).toString())
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}