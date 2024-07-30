package com.interview.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.test.model.CardItem
import com.interview.test.model.Transaction
import com.interview.test.repository.CardsRepository
import kotlinx.coroutines.launch

class CardsViewModel(private val cardsRepository: CardsRepository) : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    private val _cards = MutableLiveData<List<CardItem>>()
    val cards: LiveData<List<CardItem>> get() = _cards

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

    init {
        _cards.postValue(getMockCards())
    }

    fun getMockCards(): List<CardItem> {
        val cards = arrayListOf<CardItem>()

        cards.add(
            CardItem(
                bankName = "Dutch Bangla Bank",
                cardCategory = "Platinum Plus",
                cardExpMonth = "01",
                cardExpYear = "22",
                cardHolderName = "Sunny Aveiro",
                cardNumber = "**** **** **** 1690"
            )
        )
        cards.add(
            CardItem(
                bankName = "Dutch Bangla Bank",
                cardCategory = "Platinum Plus",
                cardExpMonth = "01",
                cardExpYear = "22",
                cardHolderName = "Richard Parker",
                cardNumber = "**** **** **** 1691"
            )
        )
        cards.add(
            CardItem(
                bankName = "Dutch Bangla Bank",
                cardCategory = "Platinum Plus",
                cardExpMonth = "01",
                cardExpYear = "22",
                cardHolderName = "William Jason",
                cardNumber = "**** **** **** 1692"
            )
        )

        return cards
    }
}