package com.interview.test.adapter

import androidx.databinding.ViewDataBinding
import com.interview.test.R
import com.interview.test.base.BaseRecyclerViewAdapter
import com.interview.test.databinding.ItemCardBinding
import com.interview.test.model.CardItem

class CardsAdapter(private val items: List<CardItem>) : BaseRecyclerViewAdapter<CardItem>() {

    override fun layoutId(position: Int): Int = R.layout.item_card

    override fun onBind(binding: ViewDataBinding, position: Int) {
        (binding as? ItemCardBinding)?.apply {
            binding.cardItem = items[position]
            executePendingBindings()
        }
    }

    override fun getItemCount(): Int = items.size
}