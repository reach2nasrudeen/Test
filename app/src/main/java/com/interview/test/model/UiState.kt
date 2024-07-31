package com.interview.test.model

data class UiState(
    var cardHolderName: String = "",
    var cardNumber: String = "",
    var cardType: String = "Choose one",
    var expiryDate: String = "",
    var cvv: String = ""
) {
    fun cardExpiryMonth() = expiryDate.split("/").firstOrNull() ?: "00"
    fun cardExpiryYear() = expiryDate.split("/").lastOrNull() ?: "00"
}