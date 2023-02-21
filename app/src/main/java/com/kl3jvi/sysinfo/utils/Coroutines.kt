package com.kl3jvi.sysinfo.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Runs a block of code within a [CoroutineScope], and the execution of the block is repeated
 * until the lifecycle state of the [Fragment] is below [minActiveState].
 * This function is similar to [lifecycleScope.launch] with the difference that it repeats the execution of the block
 * until the [Fragment]'s lifecycle state is below [minActiveState].
 * @param minActiveState the minimum [Lifecycle.State] required to keep the block running.
 * @param block the block of code to be executed within the [CoroutineScope].
 */
inline fun <T> Fragment.launchAndCollectWithViewLifecycle(
    flow: Flow<T>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.(T) -> Unit
) {
    viewLifecycleOwner.apply {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(minActiveState) {
                flow.collect {
                    block(it)
                }
            }
        }
    }
}
