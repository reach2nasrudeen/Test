package com.interview.test.viewmodel

import androidx.lifecycle.ViewModel
import com.interview.test.model.CardItem

class DashboardViewModel: ViewModel() {

    fun getMockCards(): List<CardItem> {
        val cards = arrayListOf<CardItem>()

        cards.add(
            CardItem(
                bankName = "Dutch Bangla Bank",
                cardCategory = "Platinum Plus",
                cardExpMonth = "01",
                cardExpYear = "22",
                cardNumber = "**** **** **** 1690"
            )
        )
        cards.add(
            CardItem(
                bankName = "Dutch Bangla Bank",
                cardCategory = "Platinum Plus",
                cardExpMonth = "01",
                cardExpYear = "22",
                cardNumber = "**** **** **** 1691"
            )
        )
        cards.add(
            CardItem(
                bankName = "Dutch Bangla Bank",
                cardCategory = "Platinum Plus",
                cardExpMonth = "01",
                cardExpYear = "22",
                cardNumber = "**** **** **** 1692"
            )
        )



        return cards
    }

}