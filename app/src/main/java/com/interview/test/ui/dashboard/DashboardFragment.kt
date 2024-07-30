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
import com.interview.test.viewmodel.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    private var cardsAdapter: CardsAdapter? = null
    private var _binding: FragmentDashboardBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: DashboardViewModel by viewModel<DashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardsAdapter = CardsAdapter(viewModel.getMockCards())

        binding.rvCards.adapter = cardsAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cardsAdapter = null
    }
}