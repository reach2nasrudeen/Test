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
import com.interview.test.utils.toModelString
import com.interview.test.utils.trimmedOrEmpty
import kotlinx.coroutines.launch
import timber.log.Timber

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
            _cardHolderNameState.value?.copy(
                error = uiState.cardHolderName.isEmpty(),
                message = R.string.text_error_card_holder_name_empty
            )
        }
    }

    fun validateCardNumberErrorState(reset: Boolean = false) {
        _cardNumberState.value = if (reset) {
            _cardNumberState.value?.copy(error = false)
        } else {
            _cardNumberState.value?.copy(
                error = uiState.cardNumber.isEmpty(),
                message = R.string.text_error_card_number_empty
            )
        }
    }

    fun validateCardTypeErrorState(reset: Boolean = false) {
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
            _cardExpiryState.value?.copy(
                error = uiState.expiryDate.isEmpty(),
                message = R.string.text_error_card_expiry_empty
            )
        }
    }

    fun validateCvvErrorState(reset: Boolean = false) {
        _cardCvvState.value = if (reset) {
            _cardCvvState.value?.copy(error = false)
        } else {
            _cardCvvState.value?.copy(
                error = uiState.cvv.isEmpty(),
                message = R.string.text_error_card_cvv_empty
            )
        }
    }

    private fun isFormValid(): Boolean {
        Timber.e("isFormValid----cardHolderNameState---->${cardHolderNameState.value.toModelString()}")
        Timber.e("isFormValid----cardHolderName---->${uiState.cardHolderName}")

        Timber.e("isFormValid---------------------------")
        Timber.e("isFormValid----cardNumberState---->${cardNumberState.value.toModelString()}")
        Timber.e("isFormValid----cardNumber---->${uiState.cardNumber}")

        Timber.e("isFormValid---------------------------")
        Timber.e("isFormValid----cardTypeState---->${cardTypeState.value.toModelString()}")
        Timber.e("isFormValid----cardType---->${uiState.cardType}")

        Timber.e("isFormValid---------------------------")
        Timber.e("isFormValid----cardExpiryState---->${cardExpiryState.value.toModelString()}")
        Timber.e("isFormValid----expiryDate---->${uiState.expiryDate}")

        Timber.e("isFormValid---------------------------")
        Timber.e("isFormValid----cardCvvState---->${cardCvvState.value.toModelString()}")
        Timber.e("isFormValid----cvv---->${uiState.cvv}")

        Timber.e("isFormValid---------------------------")
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
