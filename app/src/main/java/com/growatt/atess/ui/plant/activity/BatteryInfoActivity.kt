package com.growatt.atess.ui.plant.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.base.BaseViewHolder
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.databinding.ActivityBatteryInfoBinding
import com.growatt.atess.databinding.BatteryViewHolderBinding
import com.growatt.atess.model.plant.BatteryModel
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible
import com.growatt.lib.view.DividerItemDecoration

/**
 * 电池详情页面
 */
class BatteryInfoActivity : BaseActivity() {

    companion object {

        private const val KEY_BATTERY_INFO = "key_battery_info"

        fun start(context: Context?, battery: BatteryModel?) {
            context?.startActivity(Intent(context, BatteryInfoActivity::class.java).also {
                it.putExtra(KEY_BATTERY_INFO, battery)
            })
        }
    }

    private lateinit var binding: ActivityBatteryInfoBinding

    private var batteryModel: BatteryModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBatteryInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
    }

    private fun initData() {
        batteryModel = intent.getParcelableExtra(KEY_BATTERY_INFO)
    }

    private fun initView() {
        binding.rvBatteryList.addItemDecoration(
            DividerItemDecoration(
                this,
                GridLayoutManager.HORIZONTAL,
                resources.getColor(R.color.color_line)
            )
        )
        binding.rvBatteryList.addItemDecoration(
            DividerItemDecoration(
                this,
                GridLayoutManager.VERTICAL,
                resources.getColor(R.color.color_line)
            )
        )
        binding.rvBatteryList.adapter = Adapter()
        (binding.rvBatteryList.adapter as Adapter).refresh(
            batteryModel?.childList?.toMutableList() ?: emptyList()
        )
        binding.title.setTitleText(batteryModel?.batName)
    }


    inner class Adapter(var batteryList: MutableList<BatteryModel> = mutableListOf()) :
        RecyclerView.Adapter<BatteryViewHolder>(), OnItemClickListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatteryViewHolder {
            return BatteryViewHolder.create(parent, this)
        }

        override fun onBindViewHolder(holder: BatteryViewHolder, position: Int) {
            holder.bindData(batteryList[position])
        }

        override fun getItemCount(): Int {
            return batteryList.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun refresh(batteryList: List<BatteryModel>) {
            this.batteryList.clear()
            this.batteryList.addAll(batteryList)
            notifyDataSetChanged()
        }

        override fun onItemClick(v: View?, position: Int) {
            val battery = batteryList[position]
            if (battery.childList?.size ?: 0 > 0) {
                start(this@BatteryInfoActivity, battery)
            }
        }
    }
}

class BatteryViewHolder(
    itemView: View,
    onItemClickListener: OnItemClickListener
) : BaseViewHolder(itemView, onItemClickListener) {

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClickListener: OnItemClickListener
        ): BatteryViewHolder {
            val binding = BatteryViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val holder = BatteryViewHolder(binding.root, onItemClickListener)
            holder.binding = binding
            binding.root.setOnClickListener(holder)
            return holder
        }
    }

    private lateinit var binding: BatteryViewHolderBinding

    fun bindData(battery: BatteryModel) {
        binding.ivIcon.setImageResource(battery.getIcon())
        binding.tvBatteryName.text = battery.batName
        if (battery.showMaxVol()) {
            binding.tvMaxVol.visible()
            binding.tvMaxVol.text = battery.getMaxVolText()
        } else {
            binding.tvMaxVol.gone()
        }
        if (battery.showMinVol()) {
            binding.tvMinVol.visible()
            binding.tvMinVol.text = battery.getMinVolText()
        } else {
            binding.tvMinVol.gone()
        }
        if (battery.showVol()) {
            binding.tvVol.visible()
            binding.tvVol.text = battery.getVolText()
        } else {
            binding.tvVol.gone()
        }
    }

}