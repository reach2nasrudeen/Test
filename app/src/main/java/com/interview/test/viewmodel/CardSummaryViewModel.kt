package com.interview.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.interview.test.base.Constants
import com.interview.test.model.CardResponse
import com.interview.test.model.GraphType
import com.interview.test.model.SummaryUiState
import com.interview.test.model.Transaction
import com.interview.test.network.Result
import com.interview.test.network.asResult
import com.interview.test.repository.CardsRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class CardSummaryViewModel(private val cardsRepository: CardsRepository) : ViewModel() {

    private val _balance = MutableLiveData(0.0)
    val balance: LiveData<Double> get() = _balance

    private val _graphType = MutableLiveData(GraphType.MONTH)
    private val graphType: LiveData<GraphType> get() = _graphType

    private val _summaryUiState = MutableLiveData(SummaryUiState())
    val summaryUiState: LiveData<SummaryUiState> get() = _summaryUiState

    private val _transactions = MutableLiveData<List<Transaction>>(emptyList())
    val transactions: LiveData<List<Transaction>> get() = _transactions

    val isDayGraphSelected: LiveData<Boolean> get() = graphType.map { it == GraphType.DAY }
    val isMonthGraphSelected: LiveData<Boolean> get() = graphType.map { it == GraphType.MONTH }
    val isYearGraphSelected: LiveData<Boolean> get() = graphType.map { it == GraphType.YEARLY }

    fun getCardSummary() {
        viewModelScope.launch {
            cardsRepository
                .getCardSummary()
                .asResult()
                .collectLatest {
                    when (it) {
                        is Result.Loading -> {
                            _summaryUiState.postValue(SummaryUiState(loading = true))
                        }

                        is Result.Success -> {
                            _summaryUiState.postValue(SummaryUiState(success = true))
                            updateCardSummary(it.data)
                        }

                        is Result.Error -> {
                            _summaryUiState.postValue(SummaryUiState(error = true))
                        }
                    }
                }
        }
    }

    fun updateCardSummary(cardResponse: CardResponse) {
        _summaryUiState.postValue(SummaryUiState(success = true))
        _transactions.postValue(cardResponse.transactions.orEmpty())
        _balance.postValue(cardResponse.cardSummary?.currentBalance ?: 0.0)
    }

    fun updateSelectedGraphType(type: GraphType) {
        _graphType.postValue(type)
    }

    fun getTransactionsForDay(): Map<LocalDate, Double> {
        return transactions.value?.filter { transaction ->
            transaction.status == "success" && transaction.type == "debit"
        }?.groupBy {
            LocalDate.parse(
                it.date.orEmpty(),
                DateTimeFormatter.ofPattern(Constants.TF_DEFAULT_1)
            )
        }?.mapValues { entry -> entry.value.sumOf { abs(it.amount ?: 0.0) } }.orEmpty()
    }

    fun getTransactionsForMonth(): Map<LocalDate, Double> {
        return transactions.value?.filter { transaction ->
            transaction.status == "success" && transaction.type == "debit"
        }?.groupBy {
            YearMonth.from(
                LocalDate.parse(
                    it.date.orEmpty(),
                    DateTimeFormatter.ofPattern(Constants.TF_DEFAULT_1)
                )
            ).atEndOfMonth()
        }?.mapValues { entry -> entry.value.sumOf { abs(it.amount ?: 0.0) } }.orEmpty()
    }

    fun getTransactionsForYear(): Map<Int, Double> {
        val startYear = LocalDate.now().year - 1
        val endYear = LocalDate.now().year + 1


        val yearlyData = mutableMapOf<Int, Double>().apply {
            for (year in startYear..endYear) {
                this[year] = 0.0
            }
        }

        transactions.value?.filter { transaction ->
            transaction.status == "success" && transaction.type == "debit"
        }?.forEach { transaction ->
            val transactionYear = LocalDate.parse(
                transaction.date.orEmpty(),
                DateTimeFormatter.ofPattern(Constants.TF_DEFAULT_1)
            ).year

            yearlyData[transactionYear] =
                yearlyData[transactionYear]?.plus(abs(transaction.amount ?: 0.0)) ?: 0.0
        }

        return yearlyData
    }
}