package com.yuman.anotherexercise.volumedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.yuman.anotherexercise.R
import com.yuman.anotherexercise.data.YearVolumeItem
import com.yuman.anotherexercise.databinding.FragmentVolumeDetailsBinding
import com.yuman.anotherexercise.util.VOLUME_ITEM_KEY
import java.text.NumberFormat

/**
 * A simple [Fragment] subclass.
 */
class VolumeDetailsFragment : Fragment() {

    lateinit var volumeData: YearVolumeItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        volumeData =
            arguments?.getParcelable<YearVolumeItem>(VOLUME_ITEM_KEY) ?: YearVolumeItem(2000)
        val binding = DataBindingUtil.inflate<FragmentVolumeDetailsBinding>(
            inflater,
            R.layout.fragment_volume_details,
            container,
            false
        )
        binding.lifecycleOwner = activity

        binding.tvYear.text = volumeData.year.toString()
        val nf = NumberFormat.getInstance()
        nf.maximumFractionDigits = 6
        nf.isGroupingUsed = false
        nf.format(volumeData.volume).apply {
            binding.tvYearVolume.text = this
        }
        val tmpVolumeTvList = arrayOf(binding.tvQ1, binding.tvQ2, binding.tvQ3, binding.tvQ4)
        val tmpIvDrop =
            arrayOf(binding.ivDropQ1, binding.ivDropQ2, binding.ivDropQ3, binding.ivDropQ4)

        for (i in 0..3) {
            if (volumeData.quarterItems[i] == null) {
                tmpVolumeTvList[i].text = "--"
                tmpIvDrop[i].visibility = View.GONE
            } else {
                val volumeStr = nf.format(volumeData.quarterItems[i]!!.volume)
                tmpVolumeTvList[i].text = volumeStr
                if (volumeData.quarterItems[i]!!.isDropdown) {
                    tmpIvDrop[i].visibility = View.VISIBLE
                } else {
                    tmpIvDrop[i].visibility = View.GONE
                }
            }
        }

        return binding.root
    }

}
