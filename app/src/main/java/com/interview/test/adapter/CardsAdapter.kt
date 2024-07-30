package com.interview.test.adapter

import androidx.databinding.ViewDataBinding
import com.interview.test.R
import com.interview.test.base.BaseRecyclerViewAdapter
import com.interview.test.databinding.ItemCardBinding
import com.interview.test.model.CardItem
import com.interview.test.utils.calculateLuminance
import com.interview.test.utils.getRandomColor

class CardsAdapter(private var items: List<CardItem>) : BaseRecyclerViewAdapter<CardItem>() {

    var itemClickListener: ItemClickListener<CardItem>? = null

    override fun layoutId(position: Int): Int = R.layout.item_card

    override fun onBind(binding: ViewDataBinding, position: Int) {
        (binding as? ItemCardBinding)?.apply {
            val cardItem = items[position]
            binding.cardItem = cardItem
            if (position > 0) {
                binding.card.setCardBackgroundColor(cardItem.backgroundColor)
                binding.luminance = calculateLuminance(cardItem.backgroundColor)
            }
            executePendingBindings()
        }
    }

    override fun onItemClick(position: Int) {
        itemClickListener?.onItemClick(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun updateData(data: List<CardItem>) {
        items = data
        notifyAdapter()
    }

    interface ItemClickListener<T> {
        fun onItemClick(item: T)
    }
}