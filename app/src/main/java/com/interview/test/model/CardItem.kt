package com.interview.test.model

import com.interview.test.utils.getRandomColor

data class CardItem(
    val bankName: String,
    val cardNumber: String,
    val cardCategory: String,
    val cardExpMonth: String,
    val cardExpYear: String,
    val cardHolderName: String? = null,
    val cardType: CardType = CardType.VISA,
) {
    val backgroundColor: Int by lazy { getRandomColor() }
    fun formattedCardNumber(): String {
        return cardNumber
    }

    fun cardExpiryData(): String {
        return "Exp $cardExpMonth/$cardExpYear"
    }

    fun isCardHolderNameAvailable(): Boolean = !cardHolderName.isNullOrEmpty()
}

enum class CardType {
    VISA,
    MASTER,
    RUPAY
}