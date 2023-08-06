package com.kl3jvi.sysinfo.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

fun animatedMovement(view: View?) {
    // Define animation to move the card view
    val moveAnimation = ObjectAnimator.ofFloat(view, "translationX", 100f)
    moveAnimation.duration = 100 // Set duration

    // Define animation to move the card view back
    val moveBackAnimation = ObjectAnimator.ofFloat(view, "translationX", 0f)
    moveBackAnimation.duration = 300 // Set duration

    AnimatorSet().apply {
        playSequentially(moveAnimation, moveBackAnimation)
        interpolator = AccelerateDecelerateInterpolator()
        start()
    }
}
