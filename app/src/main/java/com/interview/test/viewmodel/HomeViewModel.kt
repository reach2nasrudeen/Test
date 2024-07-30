package com.interview.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.interview.test.model.CardItem
import com.interview.test.model.CardsList

class HomeViewModel : ViewModel() {

    private val _cards = MutableLiveData<List<CardItem>>()
    val cards: LiveData<List<CardItem>> get() = _cards

    private val _showBottomBar: MutableLiveData<Boolean> = MutableLiveData()
    val showBottomBar: LiveData<Boolean> = _showBottomBar


    fun updateBottomBar(show: Boolean) {
        _showBottomBar.postValue(show)
    }

    fun updateCards(cardsList: CardsList) {
        cardsList.cards.map {
            CardItem.create(
                bankName = it.bankName,
                cardNumber = it.cardNumber,
                cardCategory = it.cardCategory,
                cardExpMonth = it.cardExpMonth,
                cardExpYear = it.cardExpYear,
                cardHolderName = it.cardHolderName
            )
        }.let {
            _cards.postValue(it)
        }

    }
}