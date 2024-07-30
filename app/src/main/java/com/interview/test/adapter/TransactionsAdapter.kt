package com.interview.test.adapter

import androidx.databinding.ViewDataBinding
import com.interview.test.R
import com.interview.test.base.BaseRecyclerViewAdapter
import com.interview.test.databinding.ItemTransactionBinding
import com.interview.test.model.Transaction

class TransactionsAdapter : BaseRecyclerViewAdapter<Transaction>() {

    private var transactions: List<Transaction> = emptyList()


    override fun layoutId(position: Int): Int = R.layout.item_transaction

    override fun onBind(binding: ViewDataBinding, position: Int) {
        (binding as? ItemTransactionBinding)?.apply {

            binding.transaction = transactions[position]

            executePendingBindings()
        }
    }

    override fun updateData(data: List<Transaction>) {
        transactions = data
        notifyAdapter()
    }

    override fun getItemCount(): Int = transactions.size
}