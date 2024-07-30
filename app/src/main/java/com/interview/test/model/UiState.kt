package com.interview.test.model

data class UiState(
    var cardHolderName: String = "",
    var cardNumber: String = "",
    var cardType: String = "Choose one",
    var expiryDate: String = "",
    var cvv: String = ""
)