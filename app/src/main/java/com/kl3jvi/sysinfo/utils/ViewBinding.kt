package com.kl3jvi.sysinfo.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar
import com.example.sysinfo.R
import com.kl3jvi.sysinfo.data.model.CpuInfo

@BindingAdapter("coreNumber")
fun TextView.coreNumber(coreNumber: Int) {
    text = resources.getString(R.string.coreNumber, coreNumber.toString())
}

@BindingAdapter("integerText")
fun TextView.integerText(number: Long) {
    text = resources.getString(R.string.mhzString, number.toString())

}

@BindingAdapter("coreProgress")
fun IconRoundCornerProgressBar.progress(cpuInfo: CpuInfo.Frequency) {
    max = cpuInfo.max.toFloat()
    progress = cpuInfo.current.toFloat()
}

