package com.growatt.atess.ui.plant.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentDeviceTabBinding
import com.growatt.atess.model.plant.DeviceListResultModel
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.ui.plant.viewmodel.DeviceListViewModel
import com.growatt.lib.util.ToastUtil

/**
 * 设备列表TAB
 */
class DeviceTabFragment(
    val plantId: String?,
    private val searchWord: String = "",
    private val selectDeviceTypeChange: ((selectDeviceType: Int) -> Unit)? = null
) :
    BaseFragment() {
    private var _binding: FragmentDeviceTabBinding? = null

    private val binding get() = _binding!!

    private val viewModel: DeviceListViewModel by activityViewModels()

    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceTabBinding.inflate(inflater, container, false)
        initData()
        initView()
        return binding.root
    }

    private fun initData() {
        viewModel.getDeviceListLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
                it.first?.apply {
                    refreshViewPager(this)
                }
            } else {
                ToastUtil.show(it.second)
            }
        }

        showDialog()
        viewModel.getDeviceList(plantId, searchWord)
    }

    private fun refreshViewPager(deviceListResultModel: DeviceListResultModel) {
        tabLayoutMediator?.detach()
        tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.vpDevice) { tab, position ->
                tab.text =
                    DeviceType.getDeviceTypeName(deviceListResultModel.getDeviceTypesHasData()[position])
            }
        if (deviceListResultModel.getDeviceTypesHasData().size > 3) {
            binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        } else {
            binding.tabLayout.tabMode = TabLayout.MODE_FIXED
        }
        val fragments = mutableListOf<BaseFragment>().also {
            if (!deviceListResultModel.hpslist.isNullOrEmpty()) {
                it.add(DeviceListFragment(deviceListResultModel.hpslist))
            }
            if (!deviceListResultModel.pcslist.isNullOrEmpty()) {
                it.add(DeviceListFragment(deviceListResultModel.pcslist))
            }
            if (!deviceListResultModel.pbdlist.isNullOrEmpty()) {
                it.add(DeviceListFragment(deviceListResultModel.pbdlist))
            }
            if (!deviceListResultModel.bmslist.isNullOrEmpty()) {
                it.add(DeviceListFragment(deviceListResultModel.bmslist))
            }
            if (!deviceListResultModel.combinerList.isNullOrEmpty()) {
                it.add(DeviceListFragment(deviceListResultModel.combinerList))
            }
            if (!deviceListResultModel.datalogList.isNullOrEmpty()) {
                it.add(DeviceListFragment(deviceListResultModel.datalogList))
            }
        }
        (binding.vpDevice.adapter as Adapter).refresh(fragments)
        tabLayoutMediator?.attach()
    }

    private fun initView() {
        binding.vpDevice.adapter = Adapter(requireActivity())
        binding.vpDevice.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.getDeviceListLiveData.value?.first?.getDeviceTypesHasData()
                    ?.get(position)?.also { selectDeviceTypeChange?.invoke(it) }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class Adapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        private val fragments = mutableListOf<BaseFragment>()

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

        @SuppressLint("NotifyDataSetChanged")
        fun refresh(fragments: MutableList<BaseFragment>) {
            this.fragments.clear()
            this.fragments.addAll(fragments)
            notifyDataSetChanged()
        }
    }

}