package com.interview.test.model

data class CardSummary(
    val cardCategory: String? = null,
    val cardID: String? = null,
    val creditLimit: Int? = null,
    val currencyType: String? = null,
    val currentBalance: Double? = null
) {
    fun getFormattedBalance() = String.format("$${currentBalance}")
}