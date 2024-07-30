package com.interview.test.model


data class CardResponse(
    val cardSummary: CardSummary? = null,
    val transactions: List<Transaction?>? = null
) {
    var error: Boolean = false
    var exception: Exception? = null
}