package com.interview.test.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.interview.test.adapter.CardsAdapter
import com.interview.test.databinding.FragmentDashboardBinding
import com.interview.test.model.CardItem

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.rvCards.apply {
            adapter = CardsAdapter(getMockCards())
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun getMockCards(): List<CardItem> {
        val cards = arrayListOf<CardItem>()

        cards.add(
            CardItem(
                bankName = "Dutch Bangla Bank",
                cardCategory = "Platinum Plus",
                cardExpMonth = "01",
                cardExpYear = "22",
                cardNumber = "**** **** **** 1690"
            )
        )
        cards.add(
            CardItem(
                bankName = "Dutch Bangla Bank",
                cardCategory = "Platinum Plus",
                cardExpMonth = "01",
                cardExpYear = "22",
                cardNumber = "**** **** **** 1691"
            )
        )
        cards.add(
            CardItem(
                bankName = "Dutch Bangla Bank",
                cardCategory = "Platinum Plus",
                cardExpMonth = "01",
                cardExpYear = "22",
                cardNumber = "**** **** **** 1692"
            )
        )



        return cards
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}