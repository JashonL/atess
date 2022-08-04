package com.growatt.atess.ui.service.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.base.BaseViewHolder
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.databinding.FragmentServiceManualBinding
import com.growatt.atess.databinding.ServiceManualViewHolderBinding
import com.growatt.atess.model.plant.ServiceModel
import com.growatt.atess.ui.common.activity.WebActivity
import com.growatt.atess.ui.service.viewmodel.ServiceViewModel
import com.growatt.atess.view.itemdecoration.DividerItemDecoration
import com.growatt.lib.util.ToastUtil

/**
 * 使用手册
 */
class ServiceManualFragment : BaseFragment() {

    private var _binding: FragmentServiceManualBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ServiceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceManualBinding.inflate(layoutInflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initView() {
        binding.rvServiceManualList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                resources.getColor(R.color.color_line)
            )
        )
        binding.rvServiceManualList.adapter = Adapter()
        //item高度固定的时候，设置这个优化性能
        binding.rvServiceManualList.setHasFixedSize(true)
    }

    private fun setListener() {
        binding.srlRefresh.setOnRefreshListener {
            viewModel.getServiceManual()
        }
    }

    private fun initData() {
        viewModel.getServiceManualLiveData.observe(viewLifecycleOwner) {
            binding.srlRefresh.finishRefresh()
            if (it.second == null) {
                (binding.rvServiceManualList.adapter as Adapter).refresh(
                    it.first?.toMutableList() ?: emptyList()
                )
            } else {
                ToastUtil.show(it.second)
            }
        }
        binding.srlRefresh.autoRefresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class Adapter(var serviceList: MutableList<ServiceModel> = mutableListOf()) :
        RecyclerView.Adapter<ServiceManualViewHolder>(), OnItemClickListener {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ServiceManualViewHolder {
            return ServiceManualViewHolder.create(parent, this)
        }

        override fun onBindViewHolder(holder: ServiceManualViewHolder, position: Int) {
            holder.bindData(serviceList[position], this@ServiceManualFragment)
        }

        override fun getItemCount(): Int {
            return serviceList.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun refresh(serviceList: List<ServiceModel>) {
            this.serviceList.clear()
            this.serviceList.addAll(serviceList)
            notifyDataSetChanged()
        }

        override fun onItemClick(v: View?, position: Int) {
            val tag = v?.tag
            if (tag is ServiceModel) {
                WebActivity.start(requireContext(), tag.contentPath, tag.title)
            }
        }

    }

    class ServiceManualViewHolder(
        itemView: View,
        onItemClickListener: OnItemClickListener? = null
    ) :
        BaseViewHolder(itemView, onItemClickListener) {
        lateinit var binding: ServiceManualViewHolderBinding

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener? = null
            ): ServiceManualViewHolder {
                val binding =
                    ServiceManualViewHolderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                val holder = ServiceManualViewHolder(binding.root, onItemClickListener)
                holder.binding = binding
                holder.binding.root.setOnClickListener(holder)
                return holder
            }
        }

        fun bindData(serviceModel: ServiceModel?, fragment: BaseFragment) {
            binding.tvTitle.text = serviceModel?.title
            Glide.with(fragment).load(serviceModel?.imgPath)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.ivIcon)
            binding.root.tag = serviceModel
        }

    }
}