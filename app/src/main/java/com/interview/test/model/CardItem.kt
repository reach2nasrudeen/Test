package com.interview.test.model

data class CardItem(
    val bankName: String,
    val cardNumber: String,
    val cardCategory: String,
    val cardExpMonth: String,
    val cardExpYear: String,
    val cardHolderName: String? = null,
    val cardType: CardType = CardType.VISA
) {
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