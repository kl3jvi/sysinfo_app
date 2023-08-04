package com.kl3jvi.sysinfo.binding

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import androidx.databinding.BindingAdapter
import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar
import com.example.sysinfo.R
import com.kl3jvi.sysinfo.data.provider.CPULoad

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
