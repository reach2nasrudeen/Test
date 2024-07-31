package com.interview.test.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.interview.test.R
import com.interview.test.adapter.TransactionsAdapter
import com.interview.test.databinding.FragmentCardSummaryBinding
import com.interview.test.viewmodel.CardsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


/**
 * A simple [Fragment] subclass.
 */
class CardSummaryFragment : Fragment() {

    private val viewModel: CardsViewModel by activityViewModel<CardsViewModel>()

    private var _binding: FragmentCardSummaryBinding? = null
    private val binding
        get() = _binding!!

    private var transactionsAdapter: TransactionsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardSummaryBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.apply {
            ivBack.isVisible = true
            textTitle.setText(R.string.text_card_details)

            ivBack.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }

        transactionsAdapter = TransactionsAdapter()
        binding.rvTransactions.adapter = transactionsAdapter

        viewModel.getCardSummary()

        /*lifecycleScope.launch {
            requireContext().getObjectFromJson<CardResponse>("card_summary.json").let {
                viewModel.updateCardSummary(it)
            }
        }*/


        viewModel.transactions.observe(viewLifecycleOwner) {
            transactionsAdapter?.updateData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        transactionsAdapter = null
    }

}