package com.interview.test.repository

import com.interview.test.model.CardResponse
import com.interview.test.network.CardsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CardsRepository(private val cardsApiService: CardsApiService) {


    fun getCardSummary(): Flow<CardResponse> = flow {
        try {
            emit(
                cardsApiService.getCards()
            )
        } catch (exception: Exception) {
            emit(
                CardResponse().apply {
                    error = true
                    this.exception = exception
                }
            )
        }
    }.flowOn(Dispatchers.IO)
}