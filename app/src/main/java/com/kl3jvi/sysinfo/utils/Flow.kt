@file:OptIn(FlowPreview::class)

package com.kl3jvi.sysinfo.utils

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat

fun <T> Flow<T>.ifChanged() = ifChanged { it }

fun <T, R> Flow<T>.ifChanged(transform: (T) -> R): Flow<T> {
    var observedValueOnce = false
    var lastMappedValue: R? = null

    return filter { value ->
        val mapped = transform(value)
        if (!observedValueOnce || mapped != lastMappedValue) {
            lastMappedValue = mapped
            observedValueOnce = true
            true
        } else {
            false
        }
    }
}

fun <T, R> Flow<List<T>>.filterChanged(transform: (T) -> R): Flow<T> {
    var lastMappedValues: Map<T, R>? = null
    return flatMapConcat { values ->
        val lastMapped = lastMappedValues
        val changed = if (lastMapped == null) {
            values
        } else {
            values.filter {
                !lastMapped.containsKey(it) || lastMapped[it] != transform(it)
            }
        }
        lastMappedValues = values.map { Pair(it, transform(it)) }.toMap()
        changed.asFlow()
    }
}

fun <T, R> Flow<T>.ifAnyChanged(transform: (T) -> Array<R>): Flow<T> {
    var observedValueOnce = false
    var lastMappedValues: Array<R>? = null

    return filter { value ->
        val mapped = transform(value)
        val hasChanges = lastMappedValues
            ?.asSequence()
            ?.filterIndexed { i, r -> mapped[i] != r }
            ?.any()

        if (!observedValueOnce || hasChanges == true) {
            lastMappedValues = mapped
            observedValueOnce = true
            true
        } else {
            false
        }
    }
}
