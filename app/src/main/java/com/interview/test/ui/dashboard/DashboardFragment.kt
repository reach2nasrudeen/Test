package com.interview.test.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.interview.test.R
import com.interview.test.adapter.CardsAdapter
import com.interview.test.databinding.FragmentDashboardBinding
import com.interview.test.model.CardItem
import com.interview.test.utils.toModelString
import com.interview.test.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModel<HomeViewModel>()

    private var cardsAdapter: CardsAdapter? = null
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

        binding.toolbar.ivMenu.isVisible = true

        cardsAdapter = CardsAdapter(showMemberName = false)
        cardsAdapter?.itemClickListener = cardItemClickListener


        binding.rvCards.apply {
            adapter = cardsAdapter

            onFlingListener = null

            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(this)
        }


        homeViewModel.cards.observe(viewLifecycleOwner) {
            Timber.e(it.toModelString())
            cardsAdapter?.updateData(it)
        }
    }

    private val cardItemClickListener = object : CardsAdapter.ItemClickListener<CardItem> {
        override fun onItemClick(item: CardItem) {
            findNavController().navigate(R.id.navigation_card_summary)
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.updateBottomBar(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cardsAdapter = null
    }
}