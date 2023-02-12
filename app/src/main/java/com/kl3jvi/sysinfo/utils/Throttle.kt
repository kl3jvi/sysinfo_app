package com.kl3jvi.sysinfo.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * Returns a function that limits the executions of the [block] function, until the [skipTimeInMs]
 * passes, then the latest value passed to [block] will be used. Any calls before [skipTimeInMs]
 * passes will be ignored. All calls to the returned function must happen on the same thread.
 *
 * Credit to Terenfear https://gist.github.com/Terenfear/a84863be501d3399889455f391eeefe5
 *
 * @param skipTimeInMs the time to wait until the next call to [block] be processed.
 * @param coroutineScope the coroutine scope where [block] will executed.
 * @param block function to be execute.
 */
fun <T> throttleLatest(
    skipTimeInMs: Long = 300L,
    coroutineScope: CoroutineScope,
    block: (T) -> Unit,
): (T) -> Unit {
    var throttleJob: Job? = null
    var latestParam: T
    return { param: T ->
        latestParam = param
        if (throttleJob?.isCompleted != false) {
            throttleJob = coroutineScope.launch {
                block(latestParam)
                delay(skipTimeInMs)
            }
        }
    }
}

fun <T> throttleFirst(
    skipMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var throttleJob: Job? = null
    return { param: T ->
        if (throttleJob?.isCompleted != false) {
            throttleJob = coroutineScope.launch {
                destinationFunction(param)
                delay(skipMs)
            }
        }
    }
}