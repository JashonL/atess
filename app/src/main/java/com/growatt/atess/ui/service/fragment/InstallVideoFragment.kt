package com.growatt.atess.ui.service.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import com.growatt.atess.databinding.FragmentInstallVideoBinding
import com.growatt.atess.databinding.InstallVideoViewHolderBinding
import com.growatt.atess.model.plant.ServiceModel
import com.growatt.atess.ui.service.viewmodel.ServiceViewModel
import com.growatt.atess.view.itemdecoration.DividerItemDecoration
import com.growatt.lib.util.LogUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible

/**
 * 安装视频
 */
class InstallVideoFragment : BaseFragment() {

    private var _binding: FragmentInstallVideoBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ServiceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstallVideoBinding.inflate(layoutInflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initView() {
        binding.rvInstallVideoList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                resources.getColor(android.R.color.transparent),
                10f
            )
        )
        binding.rvInstallVideoList.adapter = Adapter()
    }

    private fun setListener() {
        binding.srlRefresh.setOnRefreshListener {
            viewModel.getInstallVideo()
        }
        binding.errorPage.root.setOnClickListener {
            binding.srlRefresh.autoRefresh()
        }
    }

    private fun initData() {
        viewModel.getInstallVideoLiveData.observe(viewLifecycleOwner) {
            binding.srlRefresh.finishRefresh()
            if (it.second == null) {
                binding.errorPage.root.gone()
                (binding.rvInstallVideoList.adapter as Adapter).refresh(
                    it.first?.toMutableList() ?: emptyList()
                )
            } else {
                binding.errorPage.root.visible()
            }
        }
        binding.srlRefresh.autoRefresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class Adapter(var serviceList: MutableList<ServiceModel> = mutableListOf()) :
        RecyclerView.Adapter<InstallVideoViewHolder>(), OnItemClickListener {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): InstallVideoViewHolder {
            return InstallVideoViewHolder.create(parent, this)
        }

        override fun onBindViewHolder(holder: InstallVideoViewHolder, position: Int) {
            holder.bindData(serviceList[position], this@InstallVideoFragment)
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
                openWebPage(tag.contentPath)
            }
        }

        private fun openWebPage(url: String?) {
            url?.also {
                val webpage: Uri = Uri.parse(it)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    LogUtil.i(InstallVideoFragment::class.java.name, "not available browser")
                }
            }
        }
    }

    class InstallVideoViewHolder(
        itemView: View,
        onItemClickListener: OnItemClickListener? = null
    ) :
        BaseViewHolder(itemView, onItemClickListener) {
        lateinit var binding: InstallVideoViewHolderBinding

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener? = null
            ): InstallVideoViewHolder {
                val binding =
                    InstallVideoViewHolderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                val holder =
                    InstallVideoViewHolder(binding.root, onItemClickListener)
                holder.binding = binding
                holder.binding.root.setOnClickListener(holder)
                return holder
            }
        }

        fun bindData(serviceModel: ServiceModel?, fragment: BaseFragment) {
            binding.tvTitle.text = serviceModel?.title
            Glide.with(fragment).load(serviceModel?.imgPath)
                .placeholder(R.drawable.ic_placeholder_2)
                .into(binding.ivIcon)
            binding.root.tag = serviceModel
        }

    }

}