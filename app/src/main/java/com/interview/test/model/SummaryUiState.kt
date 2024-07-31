package com.interview.test.model

data class SummaryUiState(
    var loading: Boolean = false,
    var error: Boolean = false,
    var success: Boolean = false,
)