package com.interview.test.ui.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.interview.test.R
import com.interview.test.databinding.FragmentAddCardBinding
import com.interview.test.viewmodel.CardsViewModel
import com.interview.test.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */
class AddCardFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModel<HomeViewModel>()
    private val viewModel: CardsViewModel by activityViewModel<CardsViewModel>()
    private var _binding: FragmentAddCardBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.apply {
            ivBack.isVisible = true
            ivAlert.isVisible = false
            textTitle.setText(R.string.text_add_new_card)
        }
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.updateBottomBar(false)
    }

    override fun onStop() {
        super.onStop()
        homeViewModel.updateBottomBar(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}