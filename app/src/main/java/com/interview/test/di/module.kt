package com.interview.test.di

import android.content.Context
import com.interview.test.CardsApp
import com.interview.test.base.Constants
import com.interview.test.network.CardsApiService
import com.interview.test.repository.CardsRepository
import com.interview.test.viewmodel.CardsViewModel
import com.interview.test.viewmodel.DashboardViewModel
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File
import java.util.concurrent.TimeUnit

val viewModelModule = module {
    viewModel { DashboardViewModel() }
    viewModel { CardsViewModel(get()) }
}


val networkModule = module {

    // Provide HttpLoggingInterceptor
    single {
        HttpLoggingInterceptor().apply {
//            level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
        }
    }

    // Provide OkHttpClient
    single {
        val cacheDir = File((get<Context>() as CardsApp).cacheDir, "http")
        val cache = Cache(cacheDir, 10 * 1024 * 1024)

        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .readTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    // Provide Retrofit
    single<Retrofit> {
        Retrofit.Builder()
            .client(get())
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}


val repositoryModule = module {

    fun provideCardsApiService(retrofit: Retrofit): CardsApiService {
        return retrofit.create<CardsApiService>()
    }

    fun provideCardsRepository(cardsApiService: CardsApiService): CardsRepository {
        return CardsRepository(cardsApiService)
    }

    single {
        provideCardsApiService(get())
    }
    single {
        provideCardsRepository(get())
    }
}
