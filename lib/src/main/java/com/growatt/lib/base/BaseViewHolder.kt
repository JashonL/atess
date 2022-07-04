package com.growatt.lib.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

interface OnItemClickListener {
    fun onItemClick(v: View?, position: Int)
}