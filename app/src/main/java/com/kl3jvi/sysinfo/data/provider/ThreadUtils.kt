package com.kl3jvi.sysinfo.data.provider

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper

object ThreadUtils {
    private val looperBackgroundThread by lazy {
        HandlerThread("BackgroundThread")
    }
    private val looperBackgroundHandler by lazy {
        looperBackgroundThread.start()
        Handler(looperBackgroundThread.looper)
    }
    private val handler = Handler(Looper.getMainLooper())
    private val uiThread = Looper.getMainLooper().thread


    private fun backgroundHandler(): Handler {
        return looperBackgroundHandler
    }

    fun postToBackgroundThread(runnable: Runnable) {
        backgroundHandler().post(runnable)
    }

    fun postToBackgroundThread(runnable: () -> Unit) {
        backgroundHandler().post(runnable)
    }

    fun postToMainThread(runnable: Runnable) {
        handler.post(runnable)
    }

    fun postToMainThreadDelayed(runnable: Runnable, delayMillis: Long) {
        handler.postDelayed(runnable, delayMillis)
    }

    fun assertOnUiThread() {
        val currentThread = Thread.currentThread()
        val currentThreadId = currentThread.id
        val expectedThreadId = uiThread.id

        if (currentThreadId == expectedThreadId) {
            return
        }

        throw IllegalThreadStateException("Expected UI thread, but running on " + currentThread.name)
    }
}