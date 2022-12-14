package com.growatt.atess.ui.mine.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.base.BaseViewHolder
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.databinding.ActivitySelectAreaBinding
import com.growatt.atess.databinding.CountryViewHolderBinding
import com.growatt.atess.ui.mine.viewmodel.SelectAreaViewModel
import com.growatt.lib.util.ToastUtil

/**
 * 选择国家/地区页面
 */
class SelectAreaActivity : BaseActivity() {

    companion object {

        const val KEY_AREA = "key_area"

        fun start(context: Context?) {
            context?.startActivity(getIntent(context))
        }

        fun getIntent(context: Context?): Intent {
            return Intent(context, SelectAreaActivity::class.java)
        }
    }

    private lateinit var binding: ActivitySelectAreaBinding
    private val viewModel: SelectAreaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
    }

    private fun initView() {
        binding.rvList.adapter = Adapter()
    }

    private fun initData() {
        viewModel.areaListLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                (binding.rvList.adapter as Adapter).refresh(it.first.toMutableList())
            } else {
                ToastUtil.show(it.second)
            }
        }
        showDialog()
        viewModel.fetchAreaList()
    }

    inner class Adapter(var countryList: MutableList<String> = mutableListOf()) :
        RecyclerView.Adapter<CountryViewHolder>(), OnItemClickListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
            return CountryViewHolder.create(parent, this)
        }

        override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
            holder.bindData(countryList[position])
        }

        override fun getItemCount(): Int {
            return countryList.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun refresh(countryList: MutableList<String>) {
            this.countryList.clear()
            this.countryList.addAll(countryList)
            notifyDataSetChanged()
        }

        override fun onItemClick(v: View?, position: Int) {
            val intent = Intent()
            if (v?.tag is String) {
                intent.putExtra(KEY_AREA, v.tag as String)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

    }

    class CountryViewHolder(
        itemView: View,
        onItemClickListener: OnItemClickListener? = null
    ) :
        BaseViewHolder(itemView, onItemClickListener) {
        lateinit var binding: CountryViewHolderBinding

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener? = null
            ): CountryViewHolder {
                val binding =
                    CountryViewHolderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                val holder = CountryViewHolder(binding.root, onItemClickListener)
                holder.binding = binding
                holder.binding.root.setOnClickListener(holder)
                return holder
            }
        }

        fun bindData(area: String?) {
            binding.tvArea.text = area
            binding.root.tag = area
        }

    }
}