package com.interview.test.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.interview.test.R
import com.interview.test.base.BaseRecyclerViewAdapter
import com.interview.test.databinding.ItemCardBinding
import com.interview.test.model.CardItem
import com.interview.test.utils.calculateLuminance
import com.interview.test.utils.getRandomColor

class CardsAdapter(private val items: List<CardItem>) : BaseRecyclerViewAdapter<CardItem>() {

    override fun layoutId(position: Int): Int = R.layout.item_card

    override fun onBind(binding: ViewDataBinding, position: Int) {
        (binding as? ItemCardBinding)?.apply {
            binding.cardItem = items[position]
            val backgroundColor = getRandomColor()
            if(position > 0) {
                binding.card.setCardBackgroundColor(backgroundColor)
                val luminance = calculateLuminance(backgroundColor)
                setTextColorBasedOnBackground(luminance, binding.textBankName)
                setTextColorBasedOnBackground(luminance, binding.textCardNumber)
                setTextColorBasedOnBackground(luminance, binding.textCardCategory)
                setTextColorBasedOnBackground(luminance, binding.textCardExpiryData)
                setTextColorBasedOnBackground(luminance, binding.textCardHolderData)
            }
            executePendingBindings()
        }
    }

    // Set text color based on background color
    private fun setTextColorBasedOnBackground(luminance: Double, textView: TextView) {
        // Set text color based on luminance
        if (luminance > 0.5) {
            // Light background - set text color to black
            textView.setTextColor(Color.BLACK)
        } else {
            // Dark background - set text color to white
            textView.setTextColor(Color.WHITE)
        }
    }

    override fun getItemCount(): Int = items.size
}