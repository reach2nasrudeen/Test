package com.interview.test.ui.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.interview.test.R
import com.interview.test.databinding.FragmentAddCardBinding
import com.interview.test.model.CardType
import com.interview.test.utils.KeyboardHandler
import com.interview.test.utils.getColorRes
import com.interview.test.utils.toModelString
import com.interview.test.viewmodel.CardsViewModel
import com.interview.test.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class AddCardFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModel<HomeViewModel>()
    private val viewModel: CardsViewModel by viewModel<CardsViewModel>()

    private var _binding: FragmentAddCardBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.uiState = viewModel.uiState
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.apply {
            ivBack.isVisible = true
            ivAlert.isVisible = false
            textTitle.setText(R.string.text_add_new_card)

            ivBack.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }

        binding.btnAdd.setOnClickListener {
            Timber.e("uiState--value--->${viewModel.uiState.toModelString()}")
            viewModel.updateUiStateData(
                cardHolderName = binding.edCardHolderName.text.toString(),
                cardNumber = binding.edCardNumber.text.toString(),
                expiryData = binding.edCardExpiry.text.toString(),
                cvv = binding.edCardCvv.text.toString(),
            )
            viewModel.onAddCard()
        }

        binding.edCardType.setOnClickListener {
            KeyboardHandler.hideKeyboard(requireActivity())
            showPopupMenu(it)
        }

        binding.edCardCvv.doAfterTextChanged {
            it?.let { editable ->
                if (editable.length > CARD_CVC_TOTAL_SYMBOLS) {
                    it.delete(CARD_CVC_TOTAL_SYMBOLS, it.length)
                }
            }
        }

        binding.edCardHolderName.doOnTextChanged { _, _, _, _ ->
            viewModel.validateCardNameErrorState(true)
        }
        binding.edCardNumber.doOnTextChanged { _, _, _, _ ->
            viewModel.validateCardNumberErrorState(true)
        }
        binding.edCardExpiry.doOnTextChanged { _, _, _, _ ->
            viewModel.validateExpiryErrorState(true)
        }
        binding.edCardCvv.doOnTextChanged { _, _, _, _ ->
            viewModel.validateCvvErrorState(true)
        }

        viewModel.addCardDetail.observe(viewLifecycleOwner) {
            it?.let {
                homeViewModel.updateCard(it)
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)

        CardType.entries.forEachIndexed { index, cardType ->
            popupMenu.menu.add(0, cardType.ordinal, index, cardType.type)
        }

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            // Handle menu item clicks
            when (item.itemId) {
                CardType.CREDIT.ordinal -> {
                    viewModel.updateCardType(CardType.CREDIT)
                    binding.edCardType.text = viewModel.uiState.cardType
                    binding.edCardType.setTextColor(requireContext().getColorRes(R.color.light_grey))
                }

                CardType.DEBIT.ordinal -> {
                    viewModel.updateCardType(CardType.DEBIT)
                    binding.edCardType.text = viewModel.uiState.cardType
                    binding.edCardType.setTextColor(requireContext().getColorRes(R.color.light_grey))
                }
            }
            true
        }

        popupMenu.show()
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

    companion object {

        private const val CARD_CVC_TOTAL_SYMBOLS = 3
    }
}