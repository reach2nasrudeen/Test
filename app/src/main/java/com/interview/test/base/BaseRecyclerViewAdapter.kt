package com.interview.test.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.interview.test.base.BaseRecyclerViewAdapter.BaseViewHolder

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BaseViewHolder(DataBindingUtil.inflate(layoutInflater, viewType, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding
        onBind(binding, position)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun notifyAdapter(position: Int = -1, isInserted: Boolean = false) {
        if (position != -1) {
            if (isInserted) {
                notifyItemInserted(position)
            } else {
                notifyItemChanged(position)
            }
        } else {
            notifyDataSetChanged()
        }
    }

    open fun onItemClick(position: Int) {

    }

    open fun updateData(data: List<T>) {}


    override fun getItemViewType(position: Int) = layoutId(position)

    abstract fun layoutId(position: Int): Int

    abstract fun onBind(binding: ViewDataBinding, position: Int)

    open class BaseViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}