package com.interview.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.test.model.CardItem
import com.interview.test.model.CardResponse
import com.interview.test.model.CardsList
import com.interview.test.model.Transaction
import com.interview.test.repository.CardsRepository
import kotlinx.coroutines.launch

class CardsViewModel(private val cardsRepository: CardsRepository) : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> get() = _balance

    fun getCardSummary() {
        viewModelScope.launch {
            cardsRepository.getCardSummary().collect {
                _transactions.postValue(it.transactions.orEmpty())
                _balance.postValue(it.cardSummary?.currentBalance ?: 0.0)
            }
        }
    }

    fun updateCardSummary(cardResponse: CardResponse) {
        _transactions.postValue(cardResponse.transactions.orEmpty())
        _balance.postValue(cardResponse.cardSummary?.currentBalance ?: 0.0)
    }
}