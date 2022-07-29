package com.growatt.atess.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.growatt.atess.databinding.FragmentHomeServiceBinding
import com.growatt.atess.ui.service.fragment.InstallVideoFragment
import com.growatt.atess.ui.service.fragment.ServiceManualFragment

/**
 * 首页-服务
 */
class HomeServiceFragment : HomeBaseFragment() {

    private var _binding: FragmentHomeServiceBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeServiceBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.tabLayout.setSelectTabPosition(0, false)
        binding.viewpager2.adapter = Adapter(this)
        binding.tabLayout.setupWithViewPager2(binding.viewpager2)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class Adapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        private val fragments = arrayOf(ServiceManualFragment(), InstallVideoFragment())

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}