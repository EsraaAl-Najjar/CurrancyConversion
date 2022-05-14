package com.softic.esraa.elnajjar.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.softic.esraa.elnajjar.data.repository.CurrencyRepository
import com.softic.esraa.elnajjar.data.model.Resource
import com.softic.esraa.elnajjar.data.model.ObjectResponse
import com.softic.esraa.elnajjar.utils.Utility.Companion.performGetOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: CurrencyRepository) : ViewModel() {

    // Get Symbols For Currencies
    val symbolsResponse: LiveData<Resource<ObjectResponse>>
    = performGetOperation(networkCall = {
        repository.fetchAllSymbolsApi() })



    //Convert Currency Amount To Another
     fun  getCurrenciesForEURToConvertForOthersLocally(base:String, symbols:String) : LiveData<Resource<ObjectResponse>>{
       return performGetOperation(networkCall = {
           repository.getConvertedCurrencyResultForCurrentAmount(base, symbols) })
   }

}