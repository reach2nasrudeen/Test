package com.interview.test.ui.cards

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.interview.test.R
import com.interview.test.databinding.FragmentAddCardBinding
import com.interview.test.model.CardType
import com.interview.test.utils.getColorRes
import com.interview.test.utils.toModelString
import com.interview.test.viewmodel.CardsViewModel
import com.interview.test.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber


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

        binding.uiState = viewModel.uiState
        binding.lifecycleOwner = viewLifecycleOwner
        binding.toolbar.apply {
            ivBack.isVisible = true
            ivAlert.isVisible = false
            textTitle.setText(R.string.text_add_new_card)
        }

        binding.btnAdd.setOnClickListener {
            Timber.e("value--->${viewModel.uiState.toModelString()}")
        }



        binding.edCardType.setOnClickListener {
            showPopupMenu(it)
        }

        binding.edCardCvv.doAfterTextChanged {
            it?.let { editable ->
                if (editable.length > CARD_CVC_TOTAL_SYMBOLS) {
                    it.delete(CARD_CVC_TOTAL_SYMBOLS, it.length)
                }
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

    private fun isInputCorrect(
        s: Editable,
        size: Int,
        dividerPosition: Int,
        divider: Char
    ): Boolean {
        var isCorrect = s.length <= size
        for (i in 0 until s.length) {
            isCorrect = if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect and (divider == s[i])
            } else {
                isCorrect and Character.isDigit(s[i])
            }
        }
        return isCorrect
    }

    private fun concatString(digits: CharArray, dividerPosition: Int, divider: Char): String {
        val formatted = StringBuilder()

        for (i in digits.indices) {
            if (digits[i].code != 0) {
                formatted.append(digits[i])
                if ((i > 0) && (i < (digits.size - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider)
                }
            }
        }

        return formatted.toString()
    }

    private fun getDigitArray(s: Editable, size: Int): CharArray {
        val digits = CharArray(size)
        var index = 0
        var i = 0
        while (i < s.length && index < size) {
            val current = s[i]
            if (Character.isDigit(current)) {
                digits[index] = current
                index++
            }
            i++
        }
        return digits
    }
}


const val CARD_CVC_TOTAL_SYMBOLS = 3


const val CARD_NUMBER_TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000
const val CARD_NUMBER_TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4
const val CARD_NUMBER_DIVIDER_MODULO =
    5 // means divider position is every 5th symbol beginning with 1
const val CARD_NUMBER_DIVIDER_POSITION =
    CARD_NUMBER_DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0
const val CARD_NUMBER_DIVIDER = '-'

const val CARD_DATE_TOTAL_SYMBOLS = 5 // size of pattern MM/YY
const val CARD_DATE_TOTAL_DIGITS = 4 // max numbers of digits in pattern: MM + YY
const val CARD_DATE_DIVIDER_MODULO =
    3 // means divider position is every 3rd symbol beginning with 1
const val CARD_DATE_DIVIDER_POSITION =
    CARD_DATE_DIVIDER_MODULO - 1 // means divider position is every 2nd symbol beginning with 0
const val CARD_DATE_DIVIDER = '/'