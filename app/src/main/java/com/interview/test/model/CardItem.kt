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
    val backgroundColor: Int
) {

    fun cardExpiryData(): String {
        return "Exp $cardExpMonth/$cardExpYear"
    }

    companion object {
        fun create(
            bankName: String,
            cardNumber: String,
            cardCategory: String,
            cardExpMonth: String,
            cardExpYear: String,
            cardHolderName: String? = null,
            cardType: CardType = CardType.VISA
        ): CardItem {
            return CardItem(
                bankName = bankName,
                cardNumber = cardNumber,
                cardCategory = cardCategory,
                cardExpMonth = cardExpMonth,
                cardExpYear = cardExpYear,
                cardHolderName = cardHolderName,
                cardType = cardType,
                backgroundColor = getRandomColor() // Control the default color here
            )
        }
    }
}

enum class CardType {
    VISA,
    MASTER,
    RUPAY
}