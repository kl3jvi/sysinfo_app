package com.kl3jvi.sysinfo.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar
import com.example.sysinfo.R
import com.kl3jvi.sysinfo.data.model.CpuInfo
import com.kl3jvi.sysinfo.data.provider.CPULoad
import com.kl3jvi.sysinfo.data.provider.RamLoad

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

@BindingAdapter("cpuIcon")
fun IconRoundCornerProgressBar.setCpuIcon(cpuLoad: CPULoad) {
    iconImageResource = when (cpuLoad) {
        CPULoad.Low -> R.drawable.ic_cpu_low
        CPULoad.Medium -> R.drawable.ic_cpu_med
        CPULoad.High -> R.drawable.ic_cpu_high
    }
}

@BindingAdapter("progressColor")
fun IconRoundCornerProgressBar.setProgressColor(cpuLoad: CPULoad) {
    val startColor = progressColor
    val endColor = when (cpuLoad) {
        CPULoad.Low -> Color.parseColor("#009688")
        CPULoad.Medium -> Color.parseColor("#FFD166")
        CPULoad.High -> Color.parseColor("#D2691E")
    }

    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
    colorAnimation.duration = 500 // duration in milliseconds
    colorAnimation.addUpdateListener { animator ->
        progressColor = animator.animatedValue as Int
    }
    colorAnimation.start()
}

@BindingAdapter("ramLoadColor")
fun CardView.setRamLoadColor(ramLoad: RamLoad?) {
    val startColor = cardBackgroundColor.defaultColor
    val endColor = when (ramLoad) {
        RamLoad.Low -> Color.parseColor("#009688")
        RamLoad.Medium -> Color.parseColor("#FFD166")
        RamLoad.High -> Color.parseColor("#D2691E")
        else -> Color.parseColor("#009688")
    }

    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
    colorAnimation.duration = 300 // duration in milliseconds
    colorAnimation.addUpdateListener { animator ->
        setCardBackgroundColor(animator.animatedValue as Int)
    }
    colorAnimation.start()
}
