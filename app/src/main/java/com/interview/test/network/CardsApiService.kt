package com.interview.test.network

import com.interview.test.base.Constants
import com.interview.test.model.CardResponse
import retrofit2.http.GET

@JvmSuppressWildcards
interface CardsApiService {

    @GET(Constants.CARDS_URL)
    suspend fun getCards(): CardResponse
}