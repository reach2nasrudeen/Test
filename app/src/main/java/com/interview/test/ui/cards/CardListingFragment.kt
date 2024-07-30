package com.interview.test.ui.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.interview.test.R
import com.interview.test.adapter.CardsAdapter
import com.interview.test.databinding.FragmentCardListingBinding
import com.interview.test.viewmodel.CardsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


/**
 * A simple [Fragment] subclass.
 */
class CardListingFragment : Fragment() {

    private var cardsAdapter: CardsAdapter? = null

    private val viewModel: CardsViewModel by activityViewModel<CardsViewModel>()

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

        binding.toolbar.apply {
            ivBack.isVisible = true
            textTitle.setText(R.string.text_all_cards)
        }
        cardsAdapter = CardsAdapter(emptyList())

        binding.rvCards.adapter = cardsAdapter

        viewModel.cards.observe(viewLifecycleOwner) {
            cardsAdapter?.updateData(it)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cardsAdapter = null
    }

}