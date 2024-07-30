package com.interview.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.test.model.CardItem
import com.interview.test.repository.CardsRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class CardsViewModel(private val cardsRepository: CardsRepository) : ViewModel() {


    fun getCardSummary() {
        viewModelScope.launch {
            cardsRepository.getCardSummary().collect {
                Timber.e("Response---->${it}")
            }
        }
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