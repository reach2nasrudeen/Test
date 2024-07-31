package com.interview.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.test.R
import com.interview.test.model.CardItem
import com.interview.test.model.CardResponse
import com.interview.test.model.CardType
import com.interview.test.model.CardUiErrorState
import com.interview.test.model.Transaction
import com.interview.test.model.UiState
import com.interview.test.repository.CardsRepository
import com.interview.test.utils.removeWhiteSpace
import com.interview.test.utils.trimmedOrEmpty
import kotlinx.coroutines.launch
import java.util.Calendar

class CardsViewModel(private val cardsRepository: CardsRepository) : ViewModel() {

    var uiState: UiState = UiState()

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    private val _cardHolderNameState = MutableLiveData(CardUiErrorState())
    val cardHolderNameState: LiveData<CardUiErrorState> get() = _cardHolderNameState

    private val _cardNumberState = MutableLiveData(CardUiErrorState())
    val cardNumberState: LiveData<CardUiErrorState> get() = _cardNumberState

    private val _cardTypeState = MutableLiveData(CardUiErrorState())
    val cardTypeState: LiveData<CardUiErrorState> get() = _cardTypeState

    private val _cardExpiryState = MutableLiveData(CardUiErrorState())
    val cardExpiryState: LiveData<CardUiErrorState> get() = _cardExpiryState

    private val _cardCvvState = MutableLiveData(CardUiErrorState())
    val cardCvvState: LiveData<CardUiErrorState> get() = _cardCvvState

    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> get() = _balance

    private val _addCardDetail = MutableLiveData<CardItem>()
    val addCardDetail: LiveData<CardItem> get() = _addCardDetail

    fun getCardSummary() {
        viewModelScope.launch {
            cardsRepository.getCardSummary().collect {
                updateCardSummary(it)
            }
        }
    }

    fun updateCardSummary(cardResponse: CardResponse) {
        _transactions.postValue(cardResponse.transactions.orEmpty())
        _balance.postValue(cardResponse.cardSummary?.currentBalance ?: 0.0)
    }

    fun updateCardType(cardType: CardType) {
        uiState = uiState.copy(cardType = cardType.type)
        validateCardTypeErrorState(true)
    }

    fun validateCardNameErrorState(reset: Boolean = false) {
        _cardHolderNameState.value = if (reset) {
            _cardHolderNameState.value?.copy(error = false)
        } else {
            if (uiState.cardHolderName.isEmpty()) {
                _cardHolderNameState.value?.copy(
                    error = true,
                    message = R.string.text_error_card_holder_name_empty
                )
            } else if (uiState.cardHolderName.length < 3) {
                _cardHolderNameState.value?.copy(
                    error = true,
                    message = R.string.text_error_card_holder_name_min_length
                )
            } else {
                _cardHolderNameState.value?.copy(error = false)
            }
        }
    }

    fun validateCardNumberErrorState(reset: Boolean = false) {
        _cardNumberState.value = if (reset) {
            _cardNumberState.value?.copy(error = false)
        } else {
            if (uiState.cardNumber.isEmpty()) {
                _cardNumberState.value?.copy(
                    error = true,
                    message = R.string.text_error_card_number_empty
                )
            } else if (uiState.cardNumber.removeWhiteSpace().length < 16) {
                _cardNumberState.value?.copy(
                    error = true,
                    message = R.string.text_error_card_number_length
                )
            } else {
                _cardNumberState.value?.copy(error = false)
            }
        }
    }

    private fun validateCardTypeErrorState(reset: Boolean = false) {
        _cardTypeState.value = if (reset) {
            _cardTypeState.value?.copy(error = false)
        } else {
            _cardTypeState.value?.copy(
                error = uiState.cardType.isEmpty() || uiState.cardType == "Choose one",
                message = R.string.text_error_card_type_empty
            )
        }
    }

    fun validateExpiryErrorState(reset: Boolean = false) {
        _cardExpiryState.value = if (reset) {
            _cardExpiryState.value?.copy(error = false)
        } else {
            validateDate().let {
                _cardExpiryState.value?.copy(
                    error = it.first,
                    message = it.second
                )
            }
        }
    }

    private fun validateDate(): Pair<Boolean, Int> {
        val expiryData = uiState.expiryDate
        val error: Boolean
        var errorId: Int = R.string.text_error_card_expiry_empty

        if (expiryData.isEmpty()) {
            error = true
            errorId = R.string.text_error_card_expiry_empty
        } else if (expiryData.length == 5) {
            val month = expiryData.substring(0, 2).toIntOrNull()
            val year = "20" + expiryData.substring(3, 5)

            if (month == null || month < 1 || month > 12) {
                error = true
                errorId = R.string.text_error_card_expiry_invalid_month
            } else {
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

                val enteredYear = year.toInt()
                if (enteredYear < currentYear || (enteredYear == currentYear && month < currentMonth)) {
                    error = true
                    errorId = R.string.text_error_card_expired
                } else {
                    error = false
                }
            }
        } else {
            error = true
            errorId = R.string.text_error_card_expiry_invalid
        }
        return Pair(error, errorId)
    }

    fun validateCvvErrorState(reset: Boolean = false) {
        _cardCvvState.value = if (reset) {
            _cardCvvState.value?.copy(error = false)
        } else {
            if (uiState.cvv.isEmpty()) {
                _cardCvvState.value?.copy(
                    error = true,
                    message = R.string.text_error_card_cvv_empty
                )
            } else if (uiState.cvv.length < 3) {
                _cardCvvState.value?.copy(
                    error = true,
                    message = R.string.text_error_card_cvv_length
                )
            } else {
                _cardCvvState.value?.copy(error = false)
            }
        }
    }

    private fun isFormValid(): Boolean {
        return !cardHolderNameState.value?.error!!
                && !cardNumberState.value?.error!!
                && !cardTypeState.value?.error!!
                && !cardExpiryState.value?.error!!
                && !cardCvvState.value?.error!!
    }

    fun onAddCard() {
        validateCardNameErrorState()
        validateCardNumberErrorState()
        validateCardTypeErrorState()
        validateExpiryErrorState()
        validateCvvErrorState()
        if (isFormValid()) {
            _addCardDetail.value = CardItem.create(
                cardHolderName = uiState.cardHolderName.trimmedOrEmpty(),
                cardNumber = uiState.cardNumber.removeWhiteSpace(),
                cardType = CardType.fromType(uiState.cardType),
                cardExpMonth = uiState.cardExpiryMonth(),
                cardExpYear = uiState.cardExpiryYear(),
            )
        }
    }

    fun updateUiStateData(
        cardHolderName: String,
        cardNumber: String,
        expiryData: String,
        cvv: String,
    ) {
        UiState(
            cardHolderName = cardHolderName,
            cardNumber = cardNumber,
            cardType = uiState.cardType,
            expiryDate = expiryData,
            cvv = cvv
        ).let {
            uiState = it
        }
    }
}
