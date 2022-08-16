package com.growatt.atess.ui.home.fragment.synopsis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPvAndLoadBinding
import com.growatt.atess.model.plant.PVAndLoadModel
import com.growatt.atess.ui.home.viewmodel.HomeSynopsisViewModel
import com.growatt.lib.util.*

/**
 * 光伏产出和负载用电
 */
class PvAndLoadFragment : BaseFragment() {

    companion object {
        /**
         * 进度条最大长度
         */
        const val MAX_PROGRESS_WIDTH_DP = 150f

        /**
         * 进度条默认长度
         */
        const val DEFAULT_PROGRESS_WIDTH_DP = 10f
    }

    private var _binding: FragmentPvAndLoadBinding? = null

    private val binding get() = _binding!!

    private val viewModel: HomeSynopsisViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPvAndLoadBinding.inflate(inflater, container, false)
        initData()
        initView()
        return binding.root
    }

    private fun initData() {
        viewModel.getPVAndLoadLiveData.observe(viewLifecycleOwner) {
            if (it.second == null) {
                showData(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    fun refresh() {
        viewModel.getPVAndLoadInfo()
    }

    private fun showData(pvAndLoadModel: PVAndLoadModel?) {
        binding.root.visible()
        binding.model = pvAndLoadModel
        pvAndLoadModel?.also {
            binding.progressPvSelf.setViewWidth(getProgressWidth(it.getPVSelfRatio()))
            binding.progressPvGrid.setViewWidth(getProgressWidth(it.getPVToGridRatio()))

            binding.progressLoadSelf.setViewWidth(getProgressWidth(it.getLoadSelfRatio()))
            binding.progressLoadTakeElectricityGrid.setViewWidth(getProgressWidth(it.getLoadFromGridPercentRadio()))
            binding.progressLoadOilEngineOutput.setViewWidth(getProgressWidth(it.getLoadFromOilEngineRatio()))

        }
    }

    /**
     * 百分比值为0的时候返回0，不为0时加上默认长度值
     * @return 长度-dp值
     */
    private fun getProgressWidth(realRatio: Int): Float {
        if (realRatio == 0) {
            return 0f
        }
        return realRatio / 100f * MAX_PROGRESS_WIDTH_DP + DEFAULT_PROGRESS_WIDTH_DP
    }

    private fun initView() {
        binding.root.gone()

        binding.progressPvSelf.background =
            ViewUtil.createShape(resources.getColor(R.color.color_green_99), 6)
        binding.progressPvGrid.background =
            ViewUtil.createShape(resources.getColor(R.color.color_green_99), 6)
        binding.progressLoadSelf.background =
            ViewUtil.createShape(resources.getColor(R.color.color_99FF6434), 6)
        binding.progressLoadTakeElectricityGrid.background =
            ViewUtil.createShape(resources.getColor(R.color.color_99FF6434), 6)
        binding.progressLoadOilEngineOutput.background =
            ViewUtil.createShape(resources.getColor(R.color.color_99FF6434), 6)

        binding.tvPvSelfPercent.background =
            ViewUtil.createShape(resources.getColor(R.color.color_green), 15)
        binding.tvPvGridPercent.background =
            ViewUtil.createShape(resources.getColor(R.color.color_green), 15)
        binding.tvLoadSelfUsePercent.background =
            ViewUtil.createShape(resources.getColor(R.color.color_FF6434), 15)
        binding.tvLoadFromGridPercent.background =
            ViewUtil.createShape(resources.getColor(R.color.color_FF6434), 15)
        binding.tvLoadFromOilEngine.background =
            ViewUtil.createShape(resources.getColor(R.color.color_FF6434), 15)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}