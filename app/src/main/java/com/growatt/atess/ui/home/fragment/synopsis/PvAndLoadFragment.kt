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
        const val MAX_PROGRESS_WIDTH_DP = 150
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
            binding.progressPvGrid.setViewWidth(getProgressWidth(100 - it.getPVSelfRatio()))

            binding.progressLoadSelf.setViewWidth(getProgressWidth(it.getLoadSelfRatio()))
            binding.progressLoadTakeElectricityGrid.setViewWidth(getProgressWidth(100 - it.getPVSelfRatio() - it.getLoadFromOilEngineRatio()))
            binding.progressLoadOilEngineOutput.setViewWidth(getProgressWidth(it.getLoadFromOilEngineRatio()))

        }
    }

    /**
     * 取5的倍数为长度,例如4得出来的值是5,5得出来的值是5,7得出来的值是10
     * @return dp值
     */
    private fun getProgressWidth(realRatio: Int): Float {
        if (realRatio % 5 == 0) {
            return (realRatio / 100 * MAX_PROGRESS_WIDTH_DP).toFloat()
        }
        return ((realRatio / 5 + 1) * 5 / 100 * MAX_PROGRESS_WIDTH_DP).toFloat()
    }

    private fun initView() {
        binding.root.gone()
        val greenBg = ViewUtil.createShape(resources.getColor(R.color.color_993FAE29), 6)
        binding.progressPvSelf.background = greenBg
        binding.progressPvGrid.background = greenBg
        val orangeBg = ViewUtil.createShape(resources.getColor(R.color.color_99FF6434), 6)
        binding.progressLoadSelf.background = orangeBg
        binding.progressLoadTakeElectricityGrid.background = orangeBg
        binding.progressLoadOilEngineOutput.background = orangeBg

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