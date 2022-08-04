package com.growatt.atess.base.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.BaseViewHolder
import com.growatt.atess.databinding.ItemLoadingViewHolderBinding

/**
 * 加载中
 */
class ItemLoadingViewHolder(itemView: View) : BaseViewHolder(itemView) {

    companion object {
        fun create(
            parent: ViewGroup,
        ): ItemLoadingViewHolder {
            val binding = ItemLoadingViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val holder = ItemLoadingViewHolder(binding.root)
            holder.binding = binding
            return holder
        }
    }

    private lateinit var binding: ItemLoadingViewHolderBinding
}