package com.interview.test.adapter

import androidx.databinding.ViewDataBinding
import com.interview.test.R
import com.interview.test.base.BaseRecyclerViewAdapter
import com.interview.test.databinding.ItemCardBinding
import com.interview.test.model.CardItem
import com.interview.test.utils.calculateLuminance
import com.interview.test.utils.getRandomColor

class CardsAdapter(private val items: List<CardItem>) : BaseRecyclerViewAdapter<CardItem>() {

    var itemClickListener: ItemClickListener<CardItem>? = null

    override fun layoutId(position: Int): Int = R.layout.item_card

    override fun onBind(binding: ViewDataBinding, position: Int) {
        (binding as? ItemCardBinding)?.apply {
            binding.cardItem = items[position]
            if (position > 0) {
                val backgroundColor = getRandomColor()
                binding.card.setCardBackgroundColor(backgroundColor)
                binding.luminance = calculateLuminance(backgroundColor)
            }
            executePendingBindings()
        }
    }

    override fun onItemClick(position: Int) {
        itemClickListener?.onItemClick(items[position])
    }

    override fun getItemCount(): Int = items.size


    interface ItemClickListener<T> {
        fun onItemClick(item: T)
    }
}