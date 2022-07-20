package com.growatt.atess.ui.plant.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.base.BaseViewHolder
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.databinding.FragmentPlantListBinding
import com.growatt.atess.databinding.PlantViewHolderBinding
import com.growatt.atess.model.plant.PlantModel
import com.growatt.atess.ui.home.viewmodel.PlantFilterViewModel
import com.growatt.atess.ui.plant.activity.AddPlantActivity
import com.growatt.atess.ui.plant.activity.PlantInfoActivity
import com.growatt.atess.ui.plant.monitor.PlantMonitor
import com.growatt.atess.ui.plant.viewmodel.PlantListViewModel
import com.growatt.atess.view.dialog.AlertDialog
import com.growatt.atess.view.dialog.OptionsDialog
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.ViewUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible
import com.growatt.lib.view.DividerItemDecoration

/**
 * 电站列表
 */
class PlantListFragment(
    private val plantStatus: Int,
    private val listener: OnPlantStatusNumChangeListener
) :
    BaseFragment() {

    private lateinit var binding: FragmentPlantListBinding
    private val viewModel: PlantListViewModel by viewModels()
    private val filterViewModel: PlantFilterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantListBinding.inflate(inflater, container, false)
        setListener()
        initData()
        initView()
        return binding.root
    }

    private fun setListener() {
        binding.srfRefresh.setOnRefreshListener {
            refresh()
        }
        binding.ivAddPlant.setOnClickListener {
            AddPlantActivity.start(requireContext())
        }
    }

    private fun initData() {
        viewModel.getPlantListLiveData.observe(viewLifecycleOwner) {
            binding.srfRefresh.finishRefresh()
            if (it.second == null) {
                (binding.rvPlant.adapter as Adapter).refresh(it.first)
                refreshEmptyView(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
        viewModel.getPlantStatusNumLiveData.observe(viewLifecycleOwner) {
            if (it.second == null) {
                listener.onPlantStatusNumChange(it.first)
            }
        }
        filterViewModel.getPlantFilterLiveData.observe(viewLifecycleOwner) {
            binding.srfRefresh.autoRefresh()
        }
        viewModel.deletePlantLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it == null) {
                PlantMonitor.notifyPlant()
            } else {
                ToastUtil.show(it)
            }
        }
        PlantMonitor.watch(viewLifecycleOwner.lifecycle) {
            binding.srfRefresh.autoRefresh()
        }
    }

    private fun refreshEmptyView(plantModels: Array<PlantModel>?) {
        if (plantModels.isNullOrEmpty()) {
            binding.llEmpty.visible()
            if (plantStatus == PlantModel.PLANT_STATUS_ALL) {
                binding.ivAddPlant.visible()
                binding.tvEmptyText.text = getString(R.string.add_plant)
            } else {
                binding.ivAddPlant.gone()
                binding.tvEmptyText.text =
                    getString(R.string.nothing_plant_format, getPlantStatusText())
            }
        } else {
            binding.llEmpty.gone()
        }
    }

    private fun refresh() {
        filterViewModel.getPlantFilterLiveData.value?.let {
            viewModel.getPlantList(plantStatus, it)
        }
    }

    private fun initView() {
        binding.rvPlant.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                resources.getColor(R.color.color_line)
            )
        )
        binding.rvPlant.adapter = Adapter()
        //item高度固定的时候，设置这个优化性能
        binding.rvPlant.setHasFixedSize(true)


    }

    private fun getPlantStatusText(): String {
        return when (plantStatus) {
            PlantModel.PLANT_STATUS_OFFLINE -> getString(R.string.offline)
            PlantModel.PLANT_STATUS_ONLINE -> getString(R.string.online)
            PlantModel.PLANT_STATUS_TROUBLE -> getString(R.string.trouble)
            else -> getString(R.string.all)
        }
    }

    /**
     * ListAdapter需要搭配DiffUtil一起使用
     */
    inner class Adapter : ListAdapter<PlantModel, PlantViewHolder>(DiffCallback()),
        OnItemClickListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
            return PlantViewHolder.create(parent, this)
        }

        override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
            holder.bindData(getItem(position), this@PlantListFragment)
        }

        override fun onItemClick(v: View?, position: Int) {
            getItem(position).id?.let { PlantInfoActivity.start(requireContext(), it) }
        }

        override fun onItemLongClick(v: View?, position: Int) {
            val title = getString(R.string.plant_manage)
            val delete = getString(R.string.delete_plant)
            val edit = getString(R.string.edit_plant)
            val options = arrayOf(delete, edit)
            OptionsDialog.show(childFragmentManager, options, title) {
                when (options[it]) {
                    delete -> {
                        AlertDialog.showDialog(
                            childFragmentManager,
                            getString(R.string.delete_plant_confirm),
                            getString(R.string.delete),
                            getString(R.string.cancel)
                        ) {
                            showDialog()
                            viewModel.deletePlant(getItem(position)?.id ?: "")
                        }
                    }
                    edit -> {
                        AddPlantActivity.start(requireActivity(), getItem(position))
                    }
                }
            }
        }

        fun refresh(plantModels: Array<PlantModel>?) {
            submitList(plantModels?.toList())
        }
    }

    class PlantViewHolder(
        itemView: View,
        onItemClickListener: OnItemClickListener,
    ) :
        BaseViewHolder(itemView, onItemClickListener) {

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener,
            ): PlantViewHolder {
                val binding = PlantViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val holder = PlantViewHolder(binding.root, onItemClickListener)
                binding.llCity.background =
                    ViewUtil.createShape(holder.getColor(R.color.color_33000000), 0, 0, 2, 2)
                binding.tvTodayText.text = MainApplication.instance().getString(
                    R.string.slash_format,
                    holder.getString(R.string.today_power),
                    holder.getString(R.string.total_power)
                )
                holder.binding = binding
                binding.root.setOnClickListener(holder)
                binding.root.setOnLongClickListener(holder)
                return holder
            }
        }

        private lateinit var binding: PlantViewHolderBinding

        fun bindData(plantModel: PlantModel, fragment: PlantListFragment) {
            Glide.with(fragment).load(plantModel.plantImgName)
                .placeholder(R.drawable.ic_placeholder)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(), RoundedCorners(ViewUtil.dp2px(fragment.requireContext(), 2f))
                    )
                )
                .into(binding.ivPlantImage)
            binding.llCity.background =
                ViewUtil.createShape(getColor(R.color.color_33000000), 0, 0, 2, 2)
            binding.tvCity.text = plantModel.city
            binding.tvPlantName.text = plantModel.plantName
            when (plantModel.hasDeviceOnLine) {
                PlantModel.PLANT_STATUS_ONLINE -> {
                    binding.tvPlantStatus.text = getString(R.string.online)
                    binding.tvPlantStatus.background =
                        ViewUtil.createShape(getColor(R.color.color_3FAE29), 8)
                }
                PlantModel.PLANT_STATUS_OFFLINE -> {
                    binding.tvPlantStatus.text = getString(R.string.offline)
                    binding.tvPlantStatus.background =
                        ViewUtil.createShape(getColor(R.color.color_bbbbbb), 8)
                }
                PlantModel.PLANT_STATUS_TROUBLE -> {
                    binding.tvPlantStatus.text = getString(R.string.trouble)
                    binding.tvPlantStatus.background =
                        ViewUtil.createShape(getColor(R.color.color_FF6434), 8)
                }
            }
            binding.llCurrentPower.background =
                ViewUtil.createShape(getColor(R.color.color_1A0087FD), 2)
            binding.tvCurrentPower.text = plantModel.currentPacStr
            binding.tvInstallDate.text = plantModel.createDateText
            binding.tvTotalComponentPower.text = plantModel.nominalPowerStr
            binding.tvPower.text = getTvSpan(plantModel)
            binding.root.tag = plantModel
        }

        private fun getTvSpan(plantModel: PlantModel): SpannableString {
            return SpannableString("${plantModel.getETodayText()}/${plantModel.getETotalText()}kWh").also {
                val span = ForegroundColorSpan(getColor(R.color.text_gray_99))
                val startPosition = it.toString().indexOf("/")
                val endPosition = startPosition + "/".length
                it.setSpan(span, startPosition, endPosition, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PlantModel>() {
        /**
         * 是不是相同item
         */
        override fun areItemsTheSame(oldItem: PlantModel, newItem: PlantModel): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * item内容是否相等
         */
        override fun areContentsTheSame(oldItem: PlantModel, newItem: PlantModel): Boolean {
            return oldItem == newItem
        }

    }

}