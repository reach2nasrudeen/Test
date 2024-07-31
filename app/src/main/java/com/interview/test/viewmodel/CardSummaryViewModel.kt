package com.interview.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.test.model.CardResponse
import com.interview.test.model.SummaryUiState
import com.interview.test.model.Transaction
import com.interview.test.network.Result
import com.interview.test.network.asResult
import com.interview.test.repository.CardsRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CardSummaryViewModel(private val cardsRepository: CardsRepository) : ViewModel() {


    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> get() = _balance

    private val _summaryUiState = MutableLiveData<SummaryUiState>()
    val summaryUiState: LiveData<SummaryUiState> get() = _summaryUiState


    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions


    fun getCardSummary() {
        viewModelScope.launch {
            cardsRepository
                .getCardSummary()
                .asResult()
                .collectLatest {
                    when (it) {
                        is Result.Loading -> {
                            _summaryUiState.postValue(SummaryUiState(loading = true))
                        }

                        is Result.Success -> {
                            _summaryUiState.postValue(SummaryUiState(success = true))
                            updateCardSummary(it.data)
                        }

                        is Result.Error -> {
                            _summaryUiState.postValue(SummaryUiState(error = true))
                        }
                    }
                }
        }
    }

    private fun updateCardSummary(cardResponse: CardResponse) {
        _transactions.postValue(cardResponse.transactions.orEmpty())
        _balance.postValue(cardResponse.cardSummary?.currentBalance ?: 0.0)
    }
}