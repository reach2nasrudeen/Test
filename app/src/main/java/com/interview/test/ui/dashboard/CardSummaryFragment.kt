package com.interview.test.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.interview.test.R
import com.interview.test.databinding.FragmentCardListingBinding
import com.interview.test.databinding.FragmentCardSummaryBinding


/**
 * A simple [Fragment] subclass.
 */
class CardSummaryFragment : Fragment() {

    private var _binding: FragmentCardSummaryBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}