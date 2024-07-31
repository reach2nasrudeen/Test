package com.interview.test.model

import com.google.gson.annotations.SerializedName

data class Transaction(
    val amount: Double? = null,
    val date: String? = null,
    val merchant: String? = null,
    val status: String? = null,
    @SerializedName("transaction_id")
    val transactionId: String? = null,
    val type: String? = null
)