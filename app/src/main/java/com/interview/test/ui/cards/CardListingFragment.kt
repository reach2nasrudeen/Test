package com.interview.test.ui.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.interview.test.adapter.CardsAdapter
import com.interview.test.databinding.FragmentCardListingBinding
import com.interview.test.model.CardItem


/**
 * A simple [Fragment] subclass.
 */
class CardListingFragment : Fragment() {


    private var _binding: FragmentCardListingBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.rvCards.adapter = CardsAdapter(getMockCards())
    }

    private fun getMockCards(): List<CardItem> {
        val cards = arrayListOf<CardItem>()

        cards.add(
            CardItem(
                bankName = "Dutch Bangla Bank",
                cardCategory = "Platinum Plus",
                cardExpMonth = "01",
                cardExpYear = "22",
                cardHolderName = "Sunny Aveiro",
                cardNumber = "**** **** **** 1690"
            )
        )
        cards.add(
            CardItem(
                bankName = "Dutch Bangla Bank",
                cardCategory = "Platinum Plus",
                cardExpMonth = "01",
                cardExpYear = "22",
                cardHolderName = "Richard Parker",
                cardNumber = "**** **** **** 1691"
            )
        )
        cards.add(
            CardItem(
                bankName = "Dutch Bangla Bank",
                cardCategory = "Platinum Plus",
                cardExpMonth = "01",
                cardExpYear = "22",
                cardHolderName = "William Jason",
                cardNumber = "**** **** **** 1692"
            )
        )

        return cards
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}