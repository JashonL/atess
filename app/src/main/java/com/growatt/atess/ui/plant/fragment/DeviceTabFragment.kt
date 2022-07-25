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
import com.google.android.material.tabs.TabLayoutMediator
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentDeviceTabBinding
import com.growatt.atess.model.plant.DeviceListResultModel
import com.growatt.atess.ui.plant.viewmodel.DeviceListViewModel
import com.growatt.lib.util.ToastUtil

/**
 * 设备列表TAB
 */
class DeviceTabFragment : BaseFragment() {

    private lateinit var binding: FragmentDeviceTabBinding

    private val viewModel: DeviceListViewModel by activityViewModels()

    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeviceTabBinding.inflate(inflater, container, false)
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
        viewModel.getDeviceList()
    }

    private fun refreshViewPager(deviceListResultModel: DeviceListResultModel) {
        tabLayoutMediator?.detach()
        tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.vpDevice) { tab, position ->
                tab.text = deviceListResultModel.getTabsText()[position]
            }
        val fragments = mutableListOf<BaseFragment>().also {
            it.add(DeviceListFragment(deviceListResultModel.hpslist))
            it.add(DeviceListFragment(deviceListResultModel.pcslist))
            it.add(DeviceListFragment(deviceListResultModel.pbdlist))
            it.add(DeviceListFragment(deviceListResultModel.bmslist))
            it.add(DeviceListFragment(deviceListResultModel.combinerList))
            it.add(DeviceListFragment(deviceListResultModel.datalogList))
        }
        (binding.vpDevice.adapter as Adapter).refresh(fragments)
        tabLayoutMediator?.attach()
    }

    private fun initView() {
        binding.vpDevice.adapter = Adapter(requireActivity())
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